package com.example.automain

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.admin.AdminActivity
import com.example.automain.auth.LoginActivity
import com.example.automain.auth.RegisterActivity
import com.example.automain.databinding.ActivityLoginBinding
import com.example.automain.databinding.ActivityMainBinding
import com.example.automain.user.UserActivity
import com.example.components.checkIsAdmin
import com.example.components.getDocumentByEmailAndDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null){
            // User is signed in
            checkIsAdmin { result ->
                if (result == 1) {
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                }
                finish()
            }
        }else{
            // No user is signed in . do nothing
        }

        binding.signin.setOnClickListener {
            Toast.makeText(this, "Login here", Toast.LENGTH_SHORT)
            val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
        }
        binding.register.setOnClickListener {
            Toast.makeText(this, "Register here", Toast.LENGTH_SHORT)
            val intent = Intent(this , RegisterActivity::class.java)
            startActivity(intent)
        }

        saveFCMToken()

    }
    private fun saveFCMToken() {
        val email = auth.currentUser?.email ?: return
        val db = FirebaseFirestore.getInstance()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM token failed", task.exception)
                Toast.makeText(this, "Fetching FCM token failed", Toast.LENGTH_SHORT)
                return@addOnCompleteListener
            }
            val token = task.result
            db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        for (document in documents) {
                            val userRef = db.collection("users").document(document.id)

                            // Update specific fields in the found document
                            userRef.update("fcmToken", token)
                                .addOnFailureListener {
                                    userRef.set(mapOf("fcmToken" to token), SetOptions.merge())
                                }
                        }
                    }

                }
        }
    }
}