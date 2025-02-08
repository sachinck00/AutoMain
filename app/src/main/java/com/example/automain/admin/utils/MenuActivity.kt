package com.example.automain.admin.utils

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.R
import com.example.automain.admin.AdminActivity
import com.example.automain.auth.LoginActivity
import com.example.automain.componentActivity.EditProfileActivity
import com.example.automain.databinding.ActivityMenuBinding
import com.example.automain.user.UserActivity
import com.example.components.fetchCurrentUserEmail
import com.example.components.getDocumentByEmailAndDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MenuActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMenuBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding  = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        if(auth.currentUser != null){
            val email = fetchCurrentUserEmail()
            getDocumentByEmailAndDB("users", email){ document ->
                if (document != null) {
                    binding.userName.text = "Hello . . ${document.get("name").toString()}"
                }
            }
            binding.userName.text = "Hi .. Admin"

        }

        binding.back.setOnClickListener {
            var intent = Intent(this , AdminActivity::class.java)
            startActivity(intent)
        }

        binding.logout.setOnClickListener{
            auth.signOut()
            var intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.add.setOnClickListener {
            var intent = Intent(this , AddServiceActivity::class.java)
            startActivity(intent)
        }
        binding.editProfile.setOnClickListener {
            var intent = Intent(this , EditProfileActivity::class.java)
            startActivity(intent)
        }

        binding.requests.setOnClickListener {
            var intent = Intent(this , RequestsActivity::class.java)
            startActivity(intent)
        }
    }
}