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
import com.example.automain.auth.LoginActivity
import com.example.automain.databinding.ActivityUserBinding
import com.example.automain.user.fragments.Services
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
        binding.logout.setOnClickListener {
            auth.signOut()
            Toast.makeText(this, "signout", Toast.LENGTH_SHORT)
            val intent = Intent(this , MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.services.setOnClickListener {
            replaceFragment(Services())
        }
    }

    private fun replaceFragment(fragment : Fragment){
        fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.frame , fragment).commit()
    }
}