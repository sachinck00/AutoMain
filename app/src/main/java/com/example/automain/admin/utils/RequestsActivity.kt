package com.example.automain.admin.utils

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automain.R
import com.example.automain.admin.utils.requestList.Requests
import com.example.automain.admin.utils.requestList.RequestsAdapter
import com.example.automain.databinding.ActivityRequestsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RequestsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRequestsBinding
    private  lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    private lateinit var recyclerView: RecyclerView
    private lateinit var requestsAdapter: RequestsAdapter
    private var requestList = mutableListOf<Requests>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRequestsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseFirestore.getInstance()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        requestsAdapter = RequestsAdapter(requestList)
        recyclerView.adapter = requestsAdapter

        fetchRequestsFromDatabase()

        binding.back.setOnClickListener {
            val intent = Intent(this , MenuActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetchRequestsFromDatabase() {
        db.collection("serviceRequests")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val item = document.toObject(Requests::class.java)
                        if (item != null) {
                            requestList.add(item)
                        }
                    }
                    requestsAdapter.notifyDataSetChanged() // Notify adapter that data has changed
                }
            }
            .addOnFailureListener {

            }
    }
}