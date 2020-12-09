package com.ikriz.eventkampus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_event.*

class DetailEvent : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var id_event: String
    private var kuotaEvent: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

        db = Firebase.firestore
        auth = Firebase.auth

        val user = auth.currentUser


        if (user != null) {
            db.collection("user").document(user.uid).get()
                .addOnSuccessListener { doc ->
                    if (doc.get("role") == "peserta") {
                        btn_daftar.visibility = View.VISIBLE
                    } else {
                        btn_daftar.visibility = View.GONE
                    }
                }
        } else {
            btn_daftar.visibility = View.GONE
        }
        btn_daftar.isEnabled = true
        btn_daftar.setOnClickListener {
            if (user != null) {
                db.collection("peserta").whereEqualTo("id_event", id_event).get()
                    .addOnSuccessListener {
                        if (!it.isEmpty) {
                            Toast.makeText(this, "Sudah terdaftar", Toast.LENGTH_LONG).show()
                            btn_daftar.apply {
                                text = "Terdaftar"
                                isEnabled = false
                            }
                        } else {
                            val eventRef = db.collection("event").document(id_event)
                            eventRef.get().addOnSuccessListener {
                                kuotaEvent = it.get("kuota").toString().toInt()
                                if (kuotaEvent > 0) {
                                    startActivity(Intent(this, DaftarEvent::class.java).apply {
                                        putExtra("id_event", id_event)
                                        putExtra("kuota_event", kuotaEvent)
                                    })
                                } else {
                                    Toast.makeText(this, "Kuota habis", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        id_event = intent.extras?.getString("id_event").toString()
        loadDetail(id_event)
    }

    private fun loadDetail(id: String) {
        db.collection("event").document(id).get().addOnSuccessListener { event ->
            judul.text = event.get("judul").toString()
            deskripsi.text = event.get("deskripsi").toString()
            kategori.text = event.get("kategori").toString()
            kuota.text = event.get("kuota").toString()
            lokasi.text = event.get("lokasi").toString()
            narahubung.text = event.get("narahubung").toString()
            if (event.get("harga") != null) {
                harga.text = event.get("harga").toString()
                layout_harga.visibility = View.VISIBLE
            }
            if (event.get("logo") != null) {
                Glide.with(this).load(event.get("logo").toString()).override(200).centerCrop()
                    .into(logo)
            }
        }
    }
}