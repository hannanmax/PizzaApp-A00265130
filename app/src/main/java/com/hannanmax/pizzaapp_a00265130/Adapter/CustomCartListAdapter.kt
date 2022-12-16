package com.hannanmax.pizzaapp_a00265130.Adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.hannanmax.pizzaapp_a00265130.Data.CartItemList
import com.hannanmax.pizzaapp_a00265130.Data.FirebaseDBHelper
import com.hannanmax.pizzaapp_a00265130.Data.PizzaItemList
import com.hannanmax.pizzaapp_a00265130.R
import com.hannanmax.pizzaapp_a00265130.databinding.CustomCartItemListLayoutBinding
import com.squareup.picasso.Picasso

class CustomCartListAdapter(
    private val context: Context,
    private val cartItemNodeArrayList: ArrayList<String>,
    private val cartItemArrayList: ArrayList<CartItemList>,
    private val pizzaItemNodeArrayList: ArrayList<String>,
    private val pizzaItemArrayList: ArrayList<PizzaItemList>) : BaseAdapter() {

    override fun getCount(): Int {
        return cartItemArrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = if (convertView == null) {
            CustomCartItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            CustomCartItemListLayoutBinding.bind(convertView)
        }

        val currentCart = cartItemArrayList[position]
        var currentPizza = pizzaItemArrayList[position]
        var currentPizzaNode = pizzaItemNodeArrayList[position]
        var currentCartNode = cartItemNodeArrayList[position]
        var count = 0
        pizzaItemNodeArrayList.forEach {
            if(cartItemArrayList[position].pizzaID == it){
                currentPizza = pizzaItemArrayList[count]
                currentPizzaNode = pizzaItemNodeArrayList[position]
                currentCartNode = cartItemNodeArrayList[position]
                return@forEach
            }
            count++
        }

        Picasso.get().load(currentPizza.img).into(binding.imgPizza)
        binding.tvPizzaTitle.text = currentPizza.Name
        binding.tvPrice.text = "Price: $" + currentCart.price.toString()
        binding.tvQ.text = currentCart.quantity.toString()
        binding.tvSize.text = "Size: " + currentCart.size.toString()
        if(currentCart.pizzaID == "CustomPizza"){
            binding.tvPizzaTitle.text = "Custom Pizza"
            Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/pizzaapp-a00265130.appspot.com/o/CUSTOM-removebg-preview.png?alt=media&token=317df246-458b-4ed9-bd8a-ffb87905aae5").into(binding.imgPizza)
        }

        binding.btnPlusQ.setOnClickListener {
            binding.tvQ.text = (binding.tvQ.text.toString().toInt() + 1).toString()
            setPrice(currentCart.size.toString(), currentPizza, binding)
            val price = (binding.tvPrice.text.split("Price $")[1]).toString().toDouble()
            val firebaseDBHelper = FirebaseDBHelper()
            if(currentCart.pizzaID == "CustomPizza"){
                firebaseDBHelper.addToCart(context, "CustomPizza", price, currentCart.size.toString(), binding.tvQ.text.toString().toInt())
            } else {
                firebaseDBHelper.addToCart(context, currentPizzaNode, price, currentCart.size.toString(), binding.tvQ.text.toString().toInt())
            }
        }

        binding.btnMinusQ.setOnClickListener {
            if((binding.tvQ.text.toString().toInt()-1) == 0){
                AlertDialog.Builder(context)
                    .setTitle("New quantity is being 0")
                    .setMessage("Are you sure you want to remove this item?")
                    .setIcon(R.drawable.ic_baseline_warning_amber_24)
                    .setPositiveButton(
                        "Delete",
                        DialogInterface.OnClickListener { _, _ ->
                            val firebaseDBHelper = FirebaseDBHelper()
                            firebaseDBHelper.deleteCartItem(context, currentCartNode)
                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                        })
                    .setNegativeButton("NO", null).show()
            } else if(binding.tvQ.text.toString().toInt() > 0){
                binding.tvQ.text = (binding.tvQ.text.toString().toInt() - 1).toString()
                setPrice(currentCart.size.toString(), currentPizza, binding)
                val price = (binding.tvPrice.text.split("Price $")[1]).toString().toDouble()
                val firebaseDBHelper = FirebaseDBHelper()
                if(currentCart.pizzaID == "CustomPizza"){
                    firebaseDBHelper.addToCart(context, "CustomPizza", price, currentCart.size.toString(), binding.tvQ.text.toString().toInt())
                } else {
                    firebaseDBHelper.addToCart(context, currentPizzaNode, price, currentCart.size.toString(), binding.tvQ.text.toString().toInt())
                }
            }
        }

        return binding.root
    }

    private fun setPrice(size: String, currentPizza: PizzaItemList, binding: CustomCartItemListLayoutBinding) {
        val price: Float
        when (size) {
            "Small" -> {
                price = binding.tvQ.text.toString().toInt() * currentPizza.smPrice!!
                binding.tvPrice.text = "Price $$price"
            }
            "Medium" -> {
                price = binding.tvQ.text.toString().toInt() * currentPizza.mPrice!!
                binding.tvPrice.text = "Price $$price"
            }
            "Large" -> {
                price = binding.tvQ.text.toString().toInt() * currentPizza.lgPrice!!
                binding.tvPrice.text = "Price $$price"
            }
            else -> {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show()
            }
        }

    }
}