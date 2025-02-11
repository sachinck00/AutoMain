package com.example.components

import com.google.firebase.firestore.FirebaseFirestore

fun fetchAdminToken(callback: (String?) -> Unit){
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("isAdmin", true)
        .get()
        .addOnSuccessListener { documents ->
            for (document in documents){
                if(document != null){
                    val token = document.getString("fcmToken")
                    callback(token)
                }
                else{
                    callback(null)
                }
            }
        }
}