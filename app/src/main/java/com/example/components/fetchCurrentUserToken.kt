package com.example.components

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun fetchCurrentUserToken(callback: (String?) -> Unit){
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()
    val email = auth.currentUser?.email
    db.collection("users")
        .whereEqualTo("email", email)
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