package com.example.automain.componentActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.automain.R
import com.example.automain.admin.AdminActivity
import com.example.automain.databinding.ActivityProfileBinding
import com.example.automain.user.UserActivity
import com.example.components.checkIsAdmin
import com.example.components.fetchCurrentUserEmail
import com.example.components.getDocumentByEmailAndDB
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileBinding
    private lateinit var db : FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        db = FirebaseFirestore.getInstance()

        binding.back.setOnClickListener {
            checkIsAdmin { result ->
                if(result == 1){
                    var intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                }else{
                    var intent = Intent(this, UserActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        var email = fetchCurrentUserEmail()
        getDocumentByEmailAndDB("users", email){document ->
            if (document != null) {
                binding.userEmail.text = document.get("email").toString()
                binding.phone.text = document.get("phone").toString()
                binding.fullName.text = document.get("fullName").toString().capitalize()
                binding.address.text = document.get("address").toString()
                binding.alterPhone.text = document.get("alternatePhone").toString()
                binding.dob.text = document.get("dob").toString()
            }
        }



    }
}