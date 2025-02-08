package com.example.components

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

fun getNameWithEmail(email: String, callback: (String?) -> Unit){
    val db = FirebaseFirestore.getInstance()
    db.collection("users")
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                callback(document.getString("name"))
            } else {
                callback(" ")  // No document found, return null
            }
        }
        .addOnFailureListener { exception ->
            callback(" ")  // Return null on failure
        }
}