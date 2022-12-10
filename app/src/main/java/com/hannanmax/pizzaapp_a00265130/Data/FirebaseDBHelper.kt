package com.hannanmax.pizzaapp_a00265130.Data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.carousel.auna.models.SlideModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseDBHelper {

    private val database = Firebase.database
    private val myBannersRef = database.getReference("Offers/Banners")
    private val myPizzasRef = database.getReference("Pizza")
    private val myCartRef = database.getReference("Cart")
    var pizzaItemArrayList : ArrayList<PizzaItemList> = ArrayList()
    var cartItemArrayList : ArrayList<CartItemList> = ArrayList()
    var pizzaItemNodeArrayList : ArrayList<String> = ArrayList()
    var cartItemNodeArrayList : ArrayList<String> = ArrayList()
    val bannerList = ArrayList<SlideModel>()

    @JvmName("getPizzaItemArrayList1")
    fun getPizzaItemArrayList(): Pair<ArrayList<String>,ArrayList<PizzaItemList>>{
        myPizzasRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    pizzaItemArrayList.clear()
                    pizzaItemNodeArrayList.clear()
                    for (ds in snapshot.children) {
                        for (mDs in ds.children) {
                            pizzaItemNodeArrayList.add(mDs.key!!)
                            val pizzaItems = mDs.getValue(PizzaItemList::class.java)
                            pizzaItemArrayList.add(pizzaItems!!)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
        return pizzaItemNodeArrayList to pizzaItemArrayList
    }

    @JvmName("getCartItemArrayList1")
    fun getCartItemArrayList(context: Context): Pair<ArrayList<String>,ArrayList<CartItemList>>{
        myCartRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    cartItemArrayList.clear()
                    cartItemNodeArrayList.clear()
                    for (ds in snapshot.children) {
                        val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
                        if (snapshot.hasChild("Cart-$currentUser")) {
                            for (mDs in ds.children) {
                                Log.d("NodeName: ", mDs.key!!)
                                cartItemNodeArrayList.add(mDs.key!!)
                                val cartItems = mDs.getValue(CartItemList::class.java)
                                cartItemArrayList.add(cartItems!!)
                            }
                        } else {
                            Toast.makeText(context, "Nothing is in the cart yet...",Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })

        return cartItemNodeArrayList to cartItemArrayList
    }

    @JvmName("getBannerList1")
    fun getBannerList(): ArrayList<SlideModel>{
        myBannersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bannerList.clear()
                for (ds in snapshot.children) {
                    val imageUrl = ds.child("img").getValue(String::class.java)
                    val imageTitle = ds.child("title").getValue(String::class.java)
                    if (imageUrl != null && imageTitle != null) {
                        bannerList.add(SlideModel(imageUrl, imageTitle,true))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
        return bannerList
    }

    fun addToCart(context: Context, pizzaID: String, price: Double, pizzaSize: String, quantity: Int) {
        myCartRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
                if (snapshot.hasChild("Cart-$currentUser")) {
                    if (snapshot.hasChild("Cart-$currentUser/item-$pizzaID$pizzaSize")) {
                        Toast.makeText(context,"Updating current pizza to cart$pizzaSize", Toast.LENGTH_SHORT).show()
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("pizzaID").setValue(pizzaID)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("price").setValue(price)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("size").setValue(pizzaSize)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("quantity").setValue(quantity)
                    } else {
                        Toast.makeText(context,"Adding new pizza to cart$pizzaSize", Toast.LENGTH_SHORT).show()
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("pizzaID").setValue(pizzaID)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("price").setValue(price)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("size").setValue(pizzaSize)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("quantity").setValue(quantity)
                    }
                } else {
                    Toast.makeText(context,"Adding First pizza to cart$pizzaSize", Toast.LENGTH_SHORT).show()
                    myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("pizzaID").setValue(pizzaID)
                    myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("price").setValue(price)
                    myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("size").setValue(pizzaSize)
                    myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("quantity").setValue(quantity)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }
}