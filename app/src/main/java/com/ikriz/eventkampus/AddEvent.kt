package com.ikriz.eventkampus

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_add_event.*

class AddEvent : AppCompatActivity() {
    private var filepath: Uri? = null
    private var storageRef: StorageReference? = null
    private lateinit var namaLogo: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        auth = FirebaseAuth.getInstance()
        btn_upload_logo.setOnClickListener {
            startFileChooser()
        }
        btn_upload.setOnClickListener {
            if (et_judul.text.isEmpty()) {
                et_judul.error = "Isi judul terlebih dahulu"
                et_judul.requestFocus()
            }
            val judul = et_judul.text.toString()
            if (et_deskripsi.text.isEmpty()) {
                et_deskripsi.error = "Isi deskripsi terlebih dahulu"
                et_deskripsi.requestFocus()
            }
            val deskripsi = et_deskripsi.text.toString()
            if (et_kuota.text.isEmpty()) {
                et_kuota.error = "Isi kuota terlebih dahulu"
                et_kuota.requestFocus()
            }
            val kuota = et_kuota.text.toString()
            if (et_lokasi.text.isEmpty()) {
                et_lokasi.error = "Isi lokasi terlebih dahulu"
                et_lokasi.requestFocus()
            }
            val lokasi = et_lokasi.text.toString()
            if (et_kategori.text.isEmpty()) {
                et_kategori.error = "Isi kategori terlebih dahulu"
                et_kategori.requestFocus()
            }
            val kategori = et_kategori.text.toString()
            if (et_narahubung.text.isEmpty()) {
                et_narahubung.error = "Isi narahubung terlebih dahulu"
                et_narahubung.requestFocus()
            }
            val narahubung = et_narahubung.text.toString()
            val db = Firebase.firestore
            if (et_harga.text.isEmpty()) {
                uploadFile()
                filepath
                val linkDownload = "https://firebasestorage.googleapis.com/v0/b/event-kampus.appspot.com/o/Logo%20Event%2F$namaLogo?alt=media"
                val data = hashMapOf(
                    "judul" to judul,
                    "deskripsi" to deskripsi,
                    "kuota" to kuota,
                    "lokasi" to lokasi,
                    "kategori" to kategori,
                    "narahubung" to narahubung,
                    "logo" to linkDownload
                )
                val eventUpload = db.collection("event").document()
                eventUpload.set(data)
            } else {
                uploadFile()
                val linkDownload = "https://firebasestorage.googleapis.com/v0/b/event-kampus.appspot.com/o/Logo%20Event%2F$namaLogo?alt=media"
                val harga = et_harga.text.toString()
                val data = hashMapOf(
                    "judul" to judul,
                    "deskripsi" to deskripsi,
                    "kuota" to kuota,
                    "lokasi" to lokasi,
                    "kategori" to kategori,
                    "narahubung" to narahubung,
                    "harga" to harga,
                    "logo" to linkDownload
                )
                val eventUpload = db.collection("event").document()
                eventUpload.set(data)
            }
            Toast.makeText(
                baseContext, "Upload Sukses",
                Toast.LENGTH_SHORT
            ).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun uploadFile() {
        storageRef = FirebaseStorage.getInstance().reference.child("Logo Event")
        if (filepath != null) {
            val pd = ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()
            val user = auth.currentUser
            namaLogo = System.currentTimeMillis().toString() +
                    user!!.uid + ".jpg"
            val imageRef = storageRef!!.child(namaLogo)
            val uploadTask = imageRef.putFile(filepath!!)
            uploadTask.addOnCompleteListener {
                pd.dismiss()
                Toast.makeText(applicationContext, "File Uploaded", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                pd.dismiss()
                Toast.makeText(applicationContext, "Can't Upload File", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun startFileChooser() {
        val img = Intent()
        img.type = "image/*"
        img.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(img, "Choose Image"), 111)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 111 && resultCode == Activity.RESULT_OK && data!!.data != null) {
            filepath = data.data
            Toast.makeText(this, "Logo added", Toast.LENGTH_SHORT).show()
        }
    }
}