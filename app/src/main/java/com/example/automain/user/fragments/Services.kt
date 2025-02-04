package com.example.automain.user.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automain.R
import com.example.automain.admin.serviceList.ServiceAdapter
import com.example.automain.databinding.FragmentServicesBinding
import com.google.api.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Services : Fragment() {
    private lateinit var binding: FragmentServicesBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var serviceAdapter: ServiceAdapter
    private val serviceItemList = mutableListOf<com.example.automain.admin.serviceList.Services>()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentServicesBinding.inflate(layoutInflater)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        serviceAdapter = ServiceAdapter(serviceItemList)
        recyclerView.adapter = serviceAdapter

        fetchFirestoreData()

        return binding.root
    }
    private fun fetchFirestoreData() {
        db.collection("services")
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    for (document in querySnapshot.documents) {
                        val item = document.toObject(com.example.automain.admin.serviceList.Services::class.java)
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

    companion object {
        fun newInstance(param1: String, param2: String) =
            Services().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}