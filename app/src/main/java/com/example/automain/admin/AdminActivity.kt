package com.example.automain.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automain.MainActivity
import com.example.automain.R
import com.example.automain.admin.serviceList.ServiceAdapter
import com.example.automain.admin.serviceList.Services
import com.example.automain.admin.utils.MenuActivity
import com.example.automain.databinding.ActivityAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAdminBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter
    private val serviceItemList = mutableListOf<Services>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        serviceAdapter = ServiceAdapter(serviceItemList)
        recyclerView.adapter = serviceAdapter

        fetchFirestoreData()

        binding.menu.setOnClickListener {
            var intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
    private fun fetchFirestoreData() {
        db.collection("services")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val item = document.toObject(Services::class.java)
                        if (item != null) {
                            serviceItemList.add(item)
                        }
                    }
                    serviceAdapter.notifyDataSetChanged() // Notify adapter that data has changed
                }
            }
            .addOnFailureListener {

            }
    }
}