package com.example.automain.admin.serviceList

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.R
import com.example.automain.admin.AdminActivity
import com.example.automain.databinding.ActivityEditServiceBinding
import com.google.firebase.firestore.FirebaseFirestore

class EditServiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditServiceBinding
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = FirebaseFirestore.getInstance()

        val serviceName = intent.getStringExtra("SERVICE_NAME")
        val serviceAmount = intent.getStringExtra("SERVICE_AMOUNT")
        val timeRequired = intent.getStringExtra("TIME_REQUIRED")
        val warranty = intent.getStringExtra("WARRANTY")
        val recommendationTime = intent.getStringExtra("RECOMMENDATION_TIME")
        val numberOfServices = intent.getStringExtra("NUMBER_OF_SERVICES")

        // Set the data to the views
        serviceName?.let {
            binding.serviceName.text = it
        }

        serviceAmount?.let {
            binding.serviceAmount.text = it
        }

        timeRequired?.let {
            binding.timeRequired.text = it
        }

        warranty?.let {
            binding.warranty.text = it
        }

        recommendationTime?.let {
            binding.recommendationTime.text = it
        }

        numberOfServices?.let {
            binding.numberOfServices.text = it
        }

        binding.back.setOnClickListener {
            var intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }
        binding.update.setOnClickListener {
            var updatedServiceName = binding.newService.text.toString()
            var updatedRequiredTime = binding.newTime.text.toString()
            var updatedWarranty = binding.newWarranty.text.toString()
            var updatedRecommendation = binding.newRecommendation.text.toString()
            var updatedRate = binding.newRate.text.toString()
            var updatedServiceCount = binding.newServicecount.text.toString()
            if (updatedServiceName !=null && updatedRequiredTime != null && updatedWarranty != null && updatedRecommendation != null && updatedRate != null && updatedServiceCount != null){
                var updated = hashMapOf(
                    "serviceName" to updatedServiceName,
                    "timeRequired" to updatedRequiredTime,
                    "warranty" to updatedWarranty,
                    "recommendationTime" to updatedRecommendation,
                    "serviceAmount" to updatedRate,
                    "numberOfServices" to updatedServiceCount
                )
                db.collection("services")
                    .whereEqualTo("serviceName", serviceName)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        if (!querySnapshot.isEmpty) {
                            for (document in querySnapshot.documents) {
                                // Get the document ID
                                val serviceId = document.id
                                // Reference the document to update it
                                val serviceRef = db.collection("services").document(serviceId)

                                // Update the document with the new data
                                serviceRef.update(updated as Map<String, Any>)
                                    .addOnSuccessListener {
                                       Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show()
                                    }
                                var intent = Intent(this, AdminActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    }

            }
            else{
                Toast.makeText(this , "fill all fields", Toast.LENGTH_SHORT).show()
            }

        }
    }
}