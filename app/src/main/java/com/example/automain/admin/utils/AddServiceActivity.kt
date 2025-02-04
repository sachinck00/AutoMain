package com.example.automain.admin.utils

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.R
import com.example.automain.auth.LoginActivity
import com.example.automain.databinding.ActivityAddServiceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddServiceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAddServiceBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.back.setOnClickListener {
            var intent = Intent(this , MenuActivity::class.java)
            startActivity(intent)
        }
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        binding.submitBtn.setOnClickListener {
            val serviceName = binding.service.text.toString()
            val timeRequired = binding.time.text.toString()
            val warranty = binding.warrranty.text.toString()
            val recommendationTime = binding.recommendation.text.toString()
            val numberOfServices = binding.servicecount.text.toString()
            val serviceAmount = binding.rate.text.toString()

            val newService = hashMapOf(
                "serviceName" to serviceName,
                "timeRequired" to timeRequired,
                "warranty" to warranty,
                "recommendationTime" to recommendationTime,
                "numberOfServices" to numberOfServices,
                "serviceAmount" to serviceAmount
            )
            db.collection("services")
                .add(newService)
                .addOnSuccessListener {
                    binding.report.text = "Details submitted"
                }
                .addOnFailureListener {

                }
            setEmpty()

        }
    }
        private fun setEmpty(){
            binding.service.text = null
            binding.time.text = null
            binding.warrranty.text = null
            binding.recommendation.text = null
            binding.servicecount.text = null
            binding.rate.text = null
        }
}