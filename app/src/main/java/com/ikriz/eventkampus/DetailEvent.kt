package com.ikriz.eventkampus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_detail_event.*

class DetailEvent : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

        db = Firebase.firestore
        auth = Firebase.auth

        val id_event = intent.extras?.getString("id").toString()

        loadDetail(id_event)

        val user = auth.currentUser

        if (user != null) {
            db.collection("user").document(user?.uid.toString()).get()
                .addOnSuccessListener {
                    if (it.get("role") == "panitia") {
                        btn_daftar.visibility = View.GONE
                    } else {
                        btn_daftar.visibility = View.VISIBLE
                    }
                }
        } else {
            btn_daftar.visibility = View.GONE
        }
    }

    private fun loadDetail(id: String) {
        db.collection("event").document(id).get().addOnSuccessListener { event ->
            judul.text = event.get("judul").toString()
            deskripsi.text = event.get("deskripsi").toString()
            kategori.text = event.get("kategori").toString()
            kuota.text = event.get("kuota").toString()
            lokasi.text = event.get("lokasi").toString()
            narahubung.text = event.get("narahubung").toString()
            if (event.get("logo") != null) {
                Glide.with(this).load(event.get("logo").toString()).override(200).centerCrop()
                    .into(logo)
            }
        }
    }
}