package com.example.automain.user.utils

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.R
import com.example.automain.admin.AdminActivity
import com.example.automain.auth.LoginActivity
import com.example.automain.componentActivity.EditProfileActivity
import com.example.automain.databinding.ActivityUserMenuBinding
import com.example.automain.user.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserMenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserMenuBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth =FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        if(auth.currentUser != null){
            val email = auth.currentUser?.email.toString()
            db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        binding.userName.text = "Hello . . ${document.get("name").toString()}"
                    }
                }
                .addOnFailureListener {
                    binding.userName.text = "Hello .. Admin"
                }
        }

        binding.back.setOnClickListener {
            var intent = Intent(this , UserActivity::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener{
            auth.signOut()
            var intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.editProfile.setOnClickListener{
            var intent = Intent(this , EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}