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
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        val currentUser = auth.currentUser
        if(currentUser != null){
            currentUser.let {
                val email = currentUser.email
                db.collection("users")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            if (document.get("isAdmin") == true) {
                                val intent = Intent(this, AdminActivity::class.java)
                                startActivity(intent)
                            }else{
                                val intent = Intent(this, UserActivity::class.java)
                                startActivity(intent)
                            }
                            Toast.makeText(
                                this,
                                "welcome ${document.get("name")}",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                    }
            }

        }else{
            /*val intent = Intent(this, LoginActivity::class.java)
             startActivity(intent)*/
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