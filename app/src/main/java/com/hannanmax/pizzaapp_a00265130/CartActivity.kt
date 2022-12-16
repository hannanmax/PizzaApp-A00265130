package com.hannanmax.pizzaapp_a00265130

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hannanmax.pizzaapp_a00265130.Adapter.CustomCartListAdapter
import com.hannanmax.pizzaapp_a00265130.Data.CartItemList
import com.hannanmax.pizzaapp_a00265130.Data.FirebaseDBHelper
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityCartBinding

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding
    private lateinit var adapter: CustomCartListAdapter
    var cartTotal:Float = 0f
    var tax:Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llHomebtn.setOnClickListener{
            val intent = Intent(applicationContext, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.llOrdersbtn.setOnClickListener{
            val intent = Intent(applicationContext, PlacedOrdersActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.llProfilebtn.setOnClickListener{
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.btnCheckout.setOnClickListener {
            val intent = Intent(applicationContext, CheckoutActivity::class.java)
            startActivity(intent)
        }

        loadCart()
    }

    // Method: Loading up Cart from firebase realtime database
    private fun loadCart() {
        val firebaseDBHelper = FirebaseDBHelper()
        val (cartItemNodeArrayList, cartItemArrayList) = firebaseDBHelper.getCartItemArrayList(applicationContext)
        val (pizzaItemNodeArrayList, pizzaItemArrayList) = firebaseDBHelper.getPizzaItemArrayList()
        val myCartRef = Firebase.database.getReference("Cart")
        myCartRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    cartItemArrayList.clear()
                    cartItemNodeArrayList.clear()
                    for (ds in snapshot.children) {
                        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
                        if (snapshot.hasChild("Cart-$currentUser")) {
                            for (mDs in ds.children) {
                                cartItemNodeArrayList.add(mDs.key!!)
                                val cartItems = mDs.getValue(CartItemList::class.java)
                                cartItemArrayList.add(cartItems!!)
                            }
                        } else {
                            Toast.makeText(applicationContext, "Nothing is in the cart yet...", Toast.LENGTH_SHORT).show()
                        }
                    }
                    adapter = CustomCartListAdapter(applicationContext, cartItemNodeArrayList, cartItemArrayList, pizzaItemNodeArrayList, pizzaItemArrayList)
                    binding.lwPizzaList.adapter = adapter
                    adapter.notifyDataSetChanged()
                    updateCartCheckOut(cartItemArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun updateCartCheckOut(cartItemArrayList: ArrayList<CartItemList>) {
        var count = 0
        Log.d("CartTotalSIZE: ", cartItemArrayList.size.toString())
        cartTotal = 0f
        for(item in cartItemArrayList){
            try {
                cartTotal += cartItemArrayList.get(count).price!!
                Log.d("CartTotal: ", item.price!!.toString())
                count++
            } catch (e: java.lang.Exception) {

            }
        }

        tax = String.format("%.02f", (cartTotal*13)/100).toFloat()

        binding.tvSubtotal.text = "Subtotal $" + String.format("%.02f", cartTotal) + " + Tax $$tax"
        binding.btnCheckout.text = "Proceed to checkout ($count Items)"
        binding.btnCheckout.isClickable = true
    }
}