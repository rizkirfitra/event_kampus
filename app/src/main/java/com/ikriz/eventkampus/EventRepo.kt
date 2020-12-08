package com.ikriz.eventkampus

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class EventRepo {
    private val firebaseAuth = Firebase.auth
    private val firebaseFirestore = Firebase.firestore

    //Auth
    fun getUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun getPostList(): Task<QuerySnapshot> {
        return firebaseFirestore.collection("event")
            .orderBy("judul")
            .get()
    }
}