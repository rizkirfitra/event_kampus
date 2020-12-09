package com.ikriz.eventkampus

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var mAdapter: FirestoreRecyclerAdapter<Events, EventsViewHolder>
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        db = Firebase.firestore

        val user = auth.currentUser
        val reference = db.collection("user").document(user?.uid.toString())

        btn_login_main.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
        }

        btn_logout_main.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, Login::class.java))
            finish()
        }

        btn_add.setOnClickListener {
            startActivity(Intent(this, AddEvent::class.java))
        }

        if (user != null) {
            btn_login_main.visibility = View.GONE
            btn_logout_main.visibility = View.VISIBLE
            reference.get().addOnSuccessListener { doc ->
                if (doc.get("role") == "panitia") {
                    btn_add.visibility = View.VISIBLE
                }
            }
        }
        setupAdapter()
    }

    private fun setupAdapter() {
        //set adapter yang akan menampilkan data pada recyclerview
        db = Firebase.firestore
        val eventCollection = db.collection("event")
        val query = eventCollection.orderBy("judul", Query.Direction.ASCENDING)
        val options = FirestoreRecyclerOptions.Builder<Events>()
            .setQuery(query, Events::class.java)
            .build()

        mAdapter = object : FirestoreRecyclerAdapter<Events, EventsViewHolder>(options) {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsViewHolder {
                return EventsViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.items, parent, false)
                )
            }

            override fun onBindViewHolder(
                viewHolder: EventsViewHolder,
                position: Int,
                model: Events
            ) {
                viewHolder.bindItem(model)
                val id = mAdapter.snapshots.getSnapshot(position).id
                viewHolder.itemView.setOnClickListener {
                    startActivity(Intent(this@MainActivity, DetailEvent::class.java).apply {
                        putExtra("id_event", id)
                    })
                }
            }
        }
        mAdapter.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = mAdapter
    }

    override fun onStart() {
        super.onStart()
        mAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mAdapter.stopListening()
    }

}