package com.example.automain

import android.content.Intent
import android.os.Bundle
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
        checkIsAdmin { result ->
            if (result == 1) {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, UserActivity::class.java)
                startActivity(intent)
            }
            Toast.makeText(
                this,
                "Welcome ",
                Toast.LENGTH_SHORT
            ).show()
            finish()
        }
        binding.signin.setOnClickListener {
            Toast.makeText(this, "signin here", Toast.LENGTH_SHORT)
            val intent = Intent(this , LoginActivity::class.java)
            startActivity(intent)
        }
        binding.register.setOnClickListener {
            Toast.makeText(this, "signin here", Toast.LENGTH_SHORT)
            val intent = Intent(this , RegisterActivity::class.java)
            startActivity(intent)
        }

    }

}