package com.ikriz.eventkampus

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_daftar_event.*

class DaftarEvent : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar_event)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        val id_event = intent.extras?.getString("id_event").toString()
        val kuota_event = intent.extras?.getInt("kuota_event").toString().toInt()
        auth = Firebase.auth
        val id_user = auth.currentUser?.uid.toString()
        daftar_submit.setOnClickListener {
            if (daftar_nama.text.isEmpty()) {
                daftar_nama.error = "Isi nama terlebih dahulu"
                daftar_nama.requestFocus()
                return@setOnClickListener
            }
            if (daftar_email.text.isEmpty()) {
                daftar_email.error = "Isi email terlebih dahulu"
                daftar_email.requestFocus()
                return@setOnClickListener
            }
            if (daftar_telepon.text.isEmpty()) {
                daftar_telepon.error = "Isi nomor telepon terlebih dahulu"
                daftar_telepon.requestFocus()
                return@setOnClickListener
            }
            val nama = daftar_nama.text.toString()
            val email = daftar_email.text.toString()
            val telepon = daftar_telepon.text.toString()

            db = Firebase.firestore
            val eventRef = db.collection("event").document(id_event)
            val uploadPeserta = db.collection("peserta").document()
            val data = hashMapOf(
                "id_event" to id_event,
                "id_user" to id_user,
                "nama" to nama,
                "email" to email,
                "nomorTlp" to telepon
            )
            uploadPeserta.set(data)
            eventRef.update("kuota", FieldValue.increment(-1)).addOnSuccessListener {
                Toast.makeText(this, "Daftar berhasil", Toast.LENGTH_LONG).show()
                onBackPressed()
                finish()
            }
        }
        daftar_cancel.setOnClickListener {
            finish()
        }
    }
}