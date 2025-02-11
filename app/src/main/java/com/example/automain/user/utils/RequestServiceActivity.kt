package com.example.automain.user.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.R
import com.example.automain.databinding.ActivityRequestServiceBinding
import com.example.automain.user.UserActivity
import com.example.components.fetchAdminToken
import com.example.components.fetchCurrentUserEmail
import com.example.components.getCurrentDateTime
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Locale

class RequestServiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRequestServiceBinding
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    val CHANNEL_ID = "channel_id"
    val CHANNEL_NAME = "channel_name"

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRequestServiceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //notification channel and builder
       createNotificationChannel()

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.applogo)
            .setContentTitle("Service Requested Successfully")
            .setContentText("Waiting for Response from Admin")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that fires when the user taps the notification.
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(this)

        // db
        db = FirebaseFirestore.getInstance()
        val serviceName = intent.getStringExtra("SERVICE_NAME")
        serviceName?.let {
            binding.serviceName.text = it
        }

        binding.back.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }

        binding.request.setOnClickListener {
            Toast.makeText(this, "please wait", Toast.LENGTH_SHORT).show()
            var serviceName = serviceName
            var vehicleName = binding.vehicleName.text.toString()
            var vehicleNumber = binding.vehicleNumber.text.toString()
            var vehicleModel = binding.VehicleModel.text.toString()
            var location = binding.locationTextView.text
            var time = getCurrentDateTime()
            var userEmail = fetchCurrentUserEmail()

            if (vehicleName != null && vehicleModel != null && vehicleNumber != null && location != null && serviceName != null && userEmail != null && time != null) {
                var request = hashMapOf(
                    "userEmail" to userEmail,
                    "serviceName" to serviceName,
                    "vehicleName" to vehicleName,
                    "vehicleModel" to vehicleModel,
                    "vehicleNumber" to vehicleNumber,
                    "location" to location,
                    "time" to time
                )

                db.collection("serviceRequests").add(request)
                    .addOnSuccessListener {
                        clear()
                        fetchAdminToken { result ->
                            if (result != null){
                                val receiverId = result
                                val service = serviceName // Example service name
                                //sendNotificationToUser(receiverId, service)
                            }
                        } // Replace with actual User B IDExample service name
                        var intent = Intent(this , UserActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            this,
                            "Service Requested successfully . wait for response",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Service not sent", Toast.LENGTH_SHORT).show()
                    }
               notificationManager.notify(0, builder)

            }else{
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
            }

        }




        //fetch location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        } else {
            // If permission is granted, get the current location
            getCurrentLocation()
        }
    }

    private fun clear(){
        binding.vehicleName.text = null
        binding.vehicleNumber.text = null
        binding.VehicleModel.text = null
    }



    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Show the latitude and longitude in a Toast
                    Toast.makeText(this, "$latitude", Toast.LENGTH_LONG).show()

                    // Get the full address from latitude and longitude
                    getAddressFromLocation(latitude, longitude)
                } else {
                    Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show()
                }
            })
    }


    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            // Get the list of addresses from the given latitude and longitude
            val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                val fullAddress = StringBuilder()

                // Add the address line if available
                address.getAddressLine(0)?.let { fullAddress.append(it) }

                // Add locality (city) if available
                if (address.locality != null) {
                    fullAddress.append(", ").append(address.locality)
                } else if (address.subLocality != null) {
                    fullAddress.append(", ").append(address.subLocality)
                } else if (address.subAdminArea != null) {
                    fullAddress.append(", ").append(address.subAdminArea)
                }

                // Add administrative area (state) if available
                address.adminArea?.let {
                    if (fullAddress.isNotEmpty()) fullAddress.append(", ")
                    fullAddress.append(it)
                }

                // Add country name if available
                address.countryName?.let {
                    if (fullAddress.isNotEmpty()) fullAddress.append(", ")
                    fullAddress.append(it)
                }
                binding.getLocationButton.setOnClickListener {
                    if (fullAddress.isNotEmpty()) {
                        binding.locationTextView.text = "Address: $fullAddress"
                    } else {
                        Toast.makeText(this, "No address found for the location", Toast.LENGTH_SHORT).show()
                    }

                }
            } else {
                Toast.makeText(this, "No address found for the location", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error getting address", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, get the location
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = " this is notification channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}