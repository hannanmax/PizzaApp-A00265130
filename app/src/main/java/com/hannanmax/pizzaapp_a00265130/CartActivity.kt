package com.hannanmax.pizzaapp_a00265130

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.hannanmax.pizzaapp_a00265130.Adapter.CustomCartListAdapter
import com.hannanmax.pizzaapp_a00265130.Data.FirebaseDBHelper
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private var adapter: CustomCartListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llHomebtn.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.llProfilebtn.setOnClickListener{
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        loadCart()
    }

    // Method: Loading up Cart from firebase realtime database
    private fun loadCart() {
        val firebaseDBHelper = FirebaseDBHelper()
        val (cartItemNodeArrayList, cartItemArrayList) = firebaseDBHelper.getCartItemArrayList(applicationContext)
        val (pizzaItemNodeArrayList, pizzaItemArrayList) = firebaseDBHelper.getPizzaItemArrayList()
        adapter = CustomCartListAdapter(this, cartItemNodeArrayList, cartItemArrayList, pizzaItemNodeArrayList, pizzaItemArrayList)
        binding.lwPizzaList.adapter = adapter
    }
}