package com.hannanmax.pizzaapp_a00265130

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityVerifyEmailBinding


class VerifyEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVerifyEmailBinding
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        val currentUser = mAuth!!.currentUser
        if (currentUser!!.isEmailVerified) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        } else {
            currentUser.sendEmailVerification()
        }

        binding.btnRefresh.setOnClickListener() {
            mAuth!!.currentUser!!.reload()
            if (mAuth!!.currentUser!!.isEmailVerified) {
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
            } else {
                Toast.makeText(
                    applicationContext,
                    "Email isn't verified yet",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.btnEmail.setOnClickListener() {
            try {
                val url = "https://mail.google.com"
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            } catch (ex: Exception) {
                Toast.makeText(
                    this,
                    "You don't have any mail application installed on your device, please open your mail through browser and confirm email...",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val currentUser = mAuth!!.currentUser
        mAuth!!.currentUser!!.reload()
        if (currentUser!!.isEmailVerified) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    override fun onRestart() {
        super.onRestart()
        val currentUser = mAuth!!.currentUser
        if (currentUser!!.isEmailVerified) {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    override fun onBackPressed() {
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }
}