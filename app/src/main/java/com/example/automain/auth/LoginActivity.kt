package com.example.automain.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.MainActivity
import com.example.automain.R
import com.example.automain.admin.AdminActivity
import com.example.automain.databinding.ActivityLoginBinding
import com.example.automain.user.UserActivity

import com.example.components.getDocumentByEmailAndDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()



        binding.loginBtn.setOnClickListener{
            val email = binding.loginEmail.text.toString()
            val password = binding.loginPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()){
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            getDocumentByEmailAndDB("users", email){ document ->
                                if (document != null) {
                                    if (document.get("isAdmin") == true) {
                                        val intent = Intent(this, AdminActivity::class.java)
                                        startActivity(intent)
                                    }else {
                                        val intent = Intent(this, UserActivity::class.java)
                                        startActivity(intent)
                                    }
                                    Toast.makeText(
                                        this,
                                        "welcome ${document.get("name")}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                }  else {
                                    Toast.makeText(
                                        this,
                                        "Authentication failed.",
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                }
                            }
                        }else {
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "No account found with this email."
                                is FirebaseAuthInvalidCredentialsException -> "Incorrect password."
                                else -> "Authentication failed."
                            }
                            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }

            }else{
                Toast.makeText(this, "fill all details", Toast.LENGTH_SHORT).show()
            }


        }
        binding.navigateToRegister.setOnClickListener {
            var intent = Intent(this , RegisterActivity::class.java)
            Toast.makeText(this, "Register here . .", Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}