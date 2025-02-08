package com.example.automain.admin.utils.requestList

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.R
import com.example.automain.admin.utils.RequestsActivity
import com.example.automain.databinding.ActivityRequestDetailsBinding
import com.example.components.getNameWithEmail

class RequestDetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRequestDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRequestDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var serviceName = intent.getStringExtra("serviceName")
        var userEmail = intent.getStringExtra("userEmail")
        var time = intent.getStringExtra("time")
        var location = intent.getStringExtra("location")
        var vehicleModel = intent.getStringExtra("vehicleModel")
        var vehicleNumber = intent.getStringExtra("vehicleNumber")
        var vehicleName = intent.getStringExtra("vehicleName")

        serviceName?.let{
            binding.serviceName.text = it
        }
        userEmail?.let{
            getNameWithEmail(it){ name ->
                binding.userEmail.text = "By $name"
            }
        }
        time?.let{
            binding.time.text = "at $it"
        }
        location?.let{
            binding.location.text = it
        }
        vehicleModel?.let{
            binding.vehicleModel.text = "Model - $it"
        }
        vehicleNumber?.let{
            binding.vehicleNumber.text = it
        }
        vehicleName?.let{
            binding.vehicleName.text = "Vehicle - $it"
        }



        binding.back.setOnClickListener {
            var intent = Intent(this, RequestsActivity::class.java)
            startActivity(intent)
        }
    }
}