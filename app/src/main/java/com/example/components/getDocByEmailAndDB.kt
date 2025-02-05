package com.example.components

import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.DocumentSnapshot


fun getDocumentByEmailAndDB(collectionName: String, email: String, callback: (DocumentSnapshot?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    db.collection(collectionName)
        .whereEqualTo("email", email)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents[0]
                callback(document)
            } else {
                callback(null)  // No document found, return null
            }
        }
        .addOnFailureListener { exception ->
            callback(null)  // Return null on failure
        }
}
 /* Usage
 val collectionName = "users"  // The Firestore collection name
val email = "john@example.com"  // The email to search for

// Call the function
getDocumentByEmail(collectionName, email) { document ->
    if (document != null) {
        // Document was found, process it
        val username = document.getString("username")  // Example of accessing a field
        println("User found: $username")
    } else {
        // No document found or error occurred
        println("No user found with email: $email")
    }
}
*/