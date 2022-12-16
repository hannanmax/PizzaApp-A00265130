package com.hannanmax.pizzaapp_a00265130

import android.annotation.SuppressLint
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
import com.hannanmax.pizzaapp_a00265130.Data.CartItemList
import com.hannanmax.pizzaapp_a00265130.Data.FirebaseDBHelper
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding

    private var cartItemArrayList : ArrayList<CartItemList> = ArrayList()
    private var cartItemNodeArrayList : ArrayList<String> = ArrayList()

    private var cartTotal:Float = 0f
    private var tax:Float = 0f
    val delivery:Float = 0f
    var cartTotalWithTax:Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadCartData()
    }

    // Method: Loading up Cart Data for placing order from firebase realtime database
    private fun loadCartData() {
        val firebaseDBHelper = FirebaseDBHelper()
        val (cartItemNodeArrayList, cartItemArrayList) = firebaseDBHelper.getCartItemArrayList(applicationContext)
        this.cartItemArrayList = cartItemArrayList
        this.cartItemNodeArrayList = cartItemNodeArrayList

        val myCartRef = Firebase.database.getReference("Cart")
        myCartRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    cartItemArrayList.clear()
                    cartItemNodeArrayList.clear()
                    for (ds in snapshot.children) {
                        Log.d("NodeName: ", ds.key!!)
                        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
                        if (snapshot.hasChild("Cart-$currentUser")) {
                            for (mDs in ds.children) {
                                Log.d("NodeName: ", mDs.key!!)
                                cartItemNodeArrayList.add(mDs.key!!)
                                val cartItems = mDs.getValue(CartItemList::class.java)
                                cartItemArrayList.add(cartItems!!)
                            }
                        } else {
                            Toast.makeText(applicationContext, "Nothing is in the cart yet...", Toast.LENGTH_SHORT).show()
                        }
                    }
                    updateCartCheckOut(cartItemArrayList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun updateCartCheckOut(cartItemArrayList: ArrayList<CartItemList>) {
        Log.d("CartTotalSIZE: ", cartItemArrayList.size.toString())
        for((count, item) in cartItemArrayList.withIndex()){
            cartTotal += cartItemArrayList[count].price!!
            Log.d("CartTotal: ", item.price!!.toString())
        }

        tax = String.format("%.02f", (cartTotal*13)/100).toFloat()
        cartTotalWithTax = String.format("%.02f", (cartTotal + tax)).toFloat()

        binding.tvItemtotal.text = "$" + String.format("%.02f", cartTotal)
        binding.tvShipping.text = "$$delivery"
        binding.tvTax.text = "$" + String.format("%.02f", tax)
        binding.tvOrdertotal.text = "$$cartTotalWithTax"

        binding.btnPlaceOrder.setOnClickListener {
            if(binding.tvPhoneNo.text.toString().isBlank() || binding.tvPhoneNo.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please input Phone Number",Toast.LENGTH_LONG).show()
                binding.tvPhoneNo.isFocusable = true
            } else if(binding.tvHouseAptNo.text.toString().isBlank() || binding.tvHouseAptNo.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please input House Number",Toast.LENGTH_LONG).show()
                binding.tvHouseAptNo.isFocusable = true
            } else if(binding.tvStreet.text.toString().isBlank() || binding.tvStreet.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please input Street",Toast.LENGTH_LONG).show()
                binding.tvStreet.isFocusable = true
            } else if(binding.tvCity.text.toString().isBlank() || binding.tvCity.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please input City",Toast.LENGTH_LONG).show()
                binding.tvCity.isFocusable = true
            } else if(binding.tvProvince.text.toString().isBlank() || binding.tvProvince.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please input Province",Toast.LENGTH_LONG).show()
                binding.tvProvince.isFocusable = true
            } else if(binding.tvPostalCode.text.toString().isBlank() || binding.tvPostalCode.text.toString().isEmpty()){
                Toast.makeText(applicationContext,"Please input Postal code",Toast.LENGTH_LONG).show()
                binding.tvPostalCode.isFocusable = true
            } else {
                val firebaseDBHelper = FirebaseDBHelper()
                firebaseDBHelper.addToOrder(applicationContext, cartTotal, delivery, tax, cartTotalWithTax, binding.tvPhoneNo.text.toString(), binding.tvHouseAptNo.text.toString(), binding.tvStreet.text.toString(), binding.tvCity.text.toString(), binding.tvProvince.text.toString(), binding.tvPostalCode.text.toString())
                Toast.makeText(applicationContext, "Order placed successfully",Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, PlacedOrdersActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }
}