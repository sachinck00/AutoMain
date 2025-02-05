package com.example.automain.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.automain.MainActivity
import com.example.automain.R
import com.example.automain.admin.utils.MenuActivity
import com.example.automain.auth.LoginActivity
import com.example.automain.componentActivity.ProfileActivity
import com.example.automain.databinding.ActivityUserBinding
import com.example.automain.user.fragments.Enquiry
import com.example.automain.user.fragments.Services
import com.example.automain.user.utils.UserMenuActivity
import com.google.firebase.auth.FirebaseAuth

class UserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var fragmentManager: FragmentManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()

        //setting a default fragment
        val defaultFragment = Services()
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame , defaultFragment).commit()

        //switching between fragments
        binding.services.setOnClickListener {
            replaceFragment(Services())
        }
        binding.customServices.setOnClickListener {
            replaceFragment(Enquiry())
        }

        binding.menu.setOnClickListener {
            var intent = Intent(this, UserMenuActivity::class.java)
            startActivity(intent)
        }
        binding.profile.setOnClickListener {
            var intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun replaceFragment(fragment : Fragment){
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame , fragment).commit()
    }
}