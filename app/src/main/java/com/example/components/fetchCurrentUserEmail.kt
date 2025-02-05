package com.example.components

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

val db : FirebaseFirestore = FirebaseFirestore.getInstance()
val auth : FirebaseAuth = FirebaseAuth.getInstance()

fun fetchCurrentUserEmail(): String {
        return auth.currentUser?.email.toString() ?: "no user"
}