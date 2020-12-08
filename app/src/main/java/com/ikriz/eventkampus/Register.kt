package com.ikriz.eventkampus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ikriz.eventkampus.Login
import com.ikriz.eventkampus.R
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.math.sign

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        db = Firebase.firestore

        btn_registerPanitia.setOnClickListener {
            signUpUser("panitia")
        }
        btn_registerPeserta.setOnClickListener {
            signUpUser("peserta")
        }
    }

    private fun signUpUser(role: String) {
        val tv_nama = findViewById<EditText>(R.id.r_namaLengkap)
        val nama = tv_nama.text.toString()
        val email = findViewById<EditText>(R.id.r_email)
        if (email.text.toString().isEmpty()) {
            email.error = "Please enter email"
            email.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }
        val password = findViewById<EditText>(R.id.r_password)
        if (password.text.toString().isEmpty()) {
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val data = hashMapOf(
                                    "role" to role,
                                    "name" to nama
                                )
                                db.collection("user").document(user.uid.toString())
                                    .set(data)
                                    .addOnSuccessListener {
                                        Log.d("TAG", "DocumentSnapshot successfully written!")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("TAG", "Error writing document", e)
                                    }
                                startActivity(Intent(this, Login::class.java))
                                finish()
                            }
                        }
                } else {
                    Toast.makeText(
                        baseContext, "Sign Up failed. Try again after some time.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}