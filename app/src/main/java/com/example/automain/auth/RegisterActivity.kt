package com.example.automain.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.R
import com.example.automain.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        if(auth.currentUser!=null){
            finish()
        }

        binding.submitBtn.setOnClickListener {
            val emailId = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmPassword = binding.confirmPassword.text.toString()
            val un = binding.userName.text.toString()
            val phone = binding.phone.text.toString()

            val isAdmin  = 0


            if (emailId.isNotEmpty() && password.isNotEmpty() && phone.isNotEmpty() &&  un.isNotEmpty() && confirmPassword.isNotEmpty() ){
                if (password == confirmPassword){
                    auth.createUserWithEmailAndPassword(emailId, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val user = hashMapOf(
                                    "name" to un,
                                    "password" to password,
                                    "email" to emailId,
                                    "phone" to phone,
                                    "isAdmin" to isAdmin
                                )
                                db.collection("users")
                                    .add(user)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Account created " , Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "User not created" , Toast.LENGTH_SHORT).show()
                                    }

                                var intent = Intent(this , LoginActivity::class.java)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Authentication failed.",
                                    Toast.LENGTH_SHORT,
                                ).show()

                            }
                        }
                    Toast.makeText(this, "password and confirm password not same" , Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please fill all details" , Toast.LENGTH_SHORT).show()
            }


        }
        binding.navigateToLogin.setOnClickListener {
            var intent = Intent(this , LoginActivity::class.java)
            Toast.makeText(this, "Login Here" , Toast.LENGTH_SHORT).show()
            startActivity(intent)
        }
    }
}