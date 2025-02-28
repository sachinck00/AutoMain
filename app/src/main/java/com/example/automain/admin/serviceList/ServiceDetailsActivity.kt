package com.example.automain.admin.serviceList

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import com.example.automain.R
import com.example.automain.databinding.ActivityServiceDetailsBinding
import com.example.automain.user.UserActivity
import com.example.automain.user.utils.RequestServiceActivity

class ServiceDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityServiceDetailsBinding
    private lateinit var fragmentManager: FragmentManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityServiceDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // Get the data from the Intent
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

        // Optionally, you can set up an onClickListener for a back button or similar action
        binding.backButton.setOnClickListener {
            var intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        binding.requestService.setOnClickListener {
            val intent = Intent(this, RequestServiceActivity::class.java).apply {
                putExtra("SERVICE_NAME", serviceName)
            }
            startActivity(intent)
        }

    }
}