package com.hannanmax.pizzaapp_a00265130

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityProfileBinding


class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llCartbtn.setOnClickListener{
            val intent = Intent(applicationContext, CartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.llOrdersbtn.setOnClickListener{
            val intent = Intent(applicationContext, PlacedOrdersActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.llHomebtn.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut();
            val intent = Intent(applicationContext, SignInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        val user = FirebaseAuth.getInstance().currentUser

        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName("HannanMax").build()
        binding.tvName.text = FirebaseAuth.getInstance().currentUser?.displayName.toString()
    }
}