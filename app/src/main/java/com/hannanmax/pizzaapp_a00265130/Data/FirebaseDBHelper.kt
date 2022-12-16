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
import java.text.SimpleDateFormat
import java.util.*


class FirebaseDBHelper {

    private val database = Firebase.database
    private val myBannersRef = database.getReference("Offers/Banners")
    private val myPizzasRef = database.getReference("Pizza")
    private val myCartRef = database.getReference("Cart")
    private val myOrdersRef = database.getReference("Orders")
    var pizzaItemArrayList : ArrayList<PizzaItemList> = ArrayList()
    var cartItemArrayList : ArrayList<CartItemList> = ArrayList()
    var pizzaItemNodeArrayList : ArrayList<String> = ArrayList()
    var cartItemNodeArrayList : ArrayList<String> = ArrayList()
    val bannerList = ArrayList<SlideModel>()
    private val currentUser = FirebaseAuth.getInstance().currentUser!!.uid

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

    @JvmName("getCartItemToOrderArrayList1")
    fun getCartItemToOrderArrayList(context: Context): Pair<ArrayList<String>,ArrayList<CartItemList>>{
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
                        Toast.makeText(context,"Updating current pizza to cart", Toast.LENGTH_SHORT).show()
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("pizzaID").setValue(pizzaID)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("price").setValue(price)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("size").setValue(pizzaSize)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("quantity").setValue(quantity)
                    } else {
                        Toast.makeText(context,"Adding new pizza to cart", Toast.LENGTH_SHORT).show()
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("pizzaID").setValue(pizzaID)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("price").setValue(price)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("size").setValue(pizzaSize)
                        myCartRef.child("Cart-$currentUser/item-$pizzaID$pizzaSize").child("quantity").setValue(quantity)
                    }
                } else {
                    Toast.makeText(context,"Adding First pizza to cart", Toast.LENGTH_SHORT).show()
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

    fun addToOrder(
        context: Context,
        cartTotal: Float,
        delivery: Float,
        tax: Float,
        cartTotalWithTax: Float,
        phoneno: String,
        houseno: String,
        streetname: String,
        city: String,
        province: String,
        postalcode: String
    ) {
        myOrdersRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val currentUser = FirebaseAuth.getInstance().currentUser!!.uid
                var uniqueID = UUID.randomUUID().toString()
                val sdf = SimpleDateFormat("dd-M-yyyy-hh:mm:ss")
                val currentDate = sdf.format(Date())
                moveFirebaseRecord("Cart/Cart-$currentUser", "Orders/Order-$currentUser/$currentDate-date-$uniqueID/orderitems/")
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("order_date").setValue(currentDate)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("cartTotal").setValue(cartTotal)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("delivery").setValue(delivery)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("tax").setValue(tax)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("cartTotalWithTax").setValue(cartTotalWithTax)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("phoneno").setValue(phoneno)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("houseno").setValue(houseno)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("streetname").setValue(streetname)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("city").setValue(city)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("province").setValue(province)
                myOrdersRef.child("Order-$currentUser/$currentDate-date-$uniqueID").child("postalcode").setValue(postalcode)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }
        })
    }

    fun moveFirebaseRecord(fromPath: String, toPath: String) {
        Log.d("PATHHHHHHHH: ", "\n$fromPath\n$toPath")
        val myFromPath = database.getReference(fromPath)
        val myToPath = database.getReference(toPath)
        myFromPath.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myToPath.setValue(dataSnapshot.value) { error, ref ->
                    if (error != null) {
                        println("Copy to order")
                    } else {
                        println("Success")
                        myCartRef.child("Cart-$currentUser").removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Copy failed")
            }
        })
    }

    fun deleteCartItem(context: Context, cartNode: String) {
        Log.d("PATH", "Cart/Cart-$currentUser/$cartNode")
        myCartRef.child("Cart-$currentUser").child(cartNode).removeValue()
    }
}