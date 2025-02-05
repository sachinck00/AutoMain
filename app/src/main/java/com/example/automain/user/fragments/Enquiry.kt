package com.example.automain.user.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.automain.R
import com.example.automain.databinding.FragmentEnquiryBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import java.util.*

class Enquiry : Fragment() {

    private lateinit var binding: FragmentEnquiryBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // Override onCreateView to handle the logic of the fragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEnquiryBinding.inflate(layoutInflater)

        // Initialize FusedLocationProviderClient to access device location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(
                requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
            )
        } else {
            // If permission is granted, get the current location
            getCurrentLocation()
        }

        return binding.root
    }

    // Function to get the current location
    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
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
            .addOnSuccessListener(requireActivity(), OnSuccessListener<Location> { location ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude

                    // Show the latitude and longitude in a Toast
                    Toast.makeText(requireContext(), "$latitude", Toast.LENGTH_LONG).show()

                    // Get the full address from latitude and longitude
                    getAddressFromLocation(latitude, longitude)
                } else {
                    Toast.makeText(requireContext(), "Location not available", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Function to get the address from latitude and longitude
    private fun getAddressFromLocation(latitude: Double, longitude: Double) {
        val geocoder = Geocoder(requireContext(), Locale.getDefault())
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

                // Check if we have built a valid address and set it in the TextView
                if (fullAddress.isNotEmpty()) {
                    binding.locationTextView.text = "Address: $fullAddress"
                } else {
                    Toast.makeText(requireContext(), "No address found for the location", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(requireContext(), "No address found for the location", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(requireContext(), "Error getting address", Toast.LENGTH_SHORT).show()
        }
    }


    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, get the location
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
        }
    }


}
