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
import com.example.automain.admin.utils.MenuActivity
import com.example.automain.databinding.ActivityEditProfileBinding
import com.example.automain.user.UserActivity
import com.example.automain.user.utils.UserMenuActivity
import com.example.components.checkIsAdmin
import com.example.components.fetchCurrentUserEmail
import com.example.components.getDocumentByEmailAndDB
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
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
                    var intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                }
                else{
                    var intent = Intent(this, UserMenuActivity::class.java)
                    startActivity(intent)
                }
            }
        }

        binding.update.setOnClickListener {
            var fullName = binding.fullName.text.toString()
            var address = binding.proper.text.toString()
            var alterPhone = binding.alterPhone.text.toString()
            var dob = binding.dob.text.toString()

            if(fullName != null && address != null && alterPhone !=null && dob != null){
                var email = fetchCurrentUserEmail()
                getDocumentByEmailAndDB("users", email){ document ->
                    if (document != null) {
                        var userid = document.id
                        var updatedProfile = hashMapOf(
                            "fullName" to fullName,
                            "address" to address,
                            "dob" to dob,
                            "alternatePhone" to alterPhone
                        )
                        db.collection("users").document(userid).update(updatedProfile as Map<String, Any>)
                            .addOnSuccessListener {
                                Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show()
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

                    } else {
                        // No document found or error occurred
                        Toast.makeText(this, "no user logged in", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}