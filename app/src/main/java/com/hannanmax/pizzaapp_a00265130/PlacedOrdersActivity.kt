package com.hannanmax.pizzaapp_a00265130

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hannanmax.pizzaapp_a00265130.Adapter.CustomCartListAdapter
import com.hannanmax.pizzaapp_a00265130.Adapter.CustomOrderesListAdapter
import com.hannanmax.pizzaapp_a00265130.Data.CartItemList
import com.hannanmax.pizzaapp_a00265130.Data.FirebaseDBHelper
import com.hannanmax.pizzaapp_a00265130.Data.OrdersItemList
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityPlacedOrdersBinding
import java.util.ArrayList

class PlacedOrdersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacedOrdersBinding
    private lateinit var adapter: CustomOrderesListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacedOrdersBinding.inflate(layoutInflater)
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

        binding.llCartbtn.setOnClickListener{
            val intent = Intent(applicationContext, CartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        loadOrders()
    }

    private fun loadOrders() {
        var ordersItemList : ArrayList<OrdersItemList> = ArrayList()
        val myOrdersRef = Firebase.database.getReference("Orders")
        myOrdersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    ordersItemList.clear()
                    for (ds in snapshot.children) {
                        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
                        if (snapshot.hasChild("Order-$currentUser")) {
                            for (mDs in ds.children) {
                                val orderItems = mDs.getValue(OrdersItemList::class.java)
                                ordersItemList.add(orderItems!!)
                            }
                        } else {
                            Toast.makeText(applicationContext, "Nothing is in the cart yet...", Toast.LENGTH_SHORT).show()
                        }
                    }
                    adapter = CustomOrderesListAdapter(applicationContext, ordersItemList)
                    binding.lwOrderList.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }
}