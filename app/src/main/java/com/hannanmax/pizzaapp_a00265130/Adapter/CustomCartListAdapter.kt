package com.hannanmax.pizzaapp_a00265130.Adapter

import android.R
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.hannanmax.pizzaapp_a00265130.Data.CartItemList
import com.hannanmax.pizzaapp_a00265130.Data.PizzaItemList
import com.hannanmax.pizzaapp_a00265130.databinding.CustomCartItemListLayoutBinding
import com.squareup.picasso.Picasso


class CustomCartListAdapter(
    private val context: Context,
    private val cartItemNodeArrayList: ArrayList<String>,
    private val cartItemArrayList: ArrayList<CartItemList>,
    private val pizzaItemNodeArrayList: ArrayList<String>,
    private val pizzaItemArrayList: ArrayList<PizzaItemList>) : BaseAdapter() {

    var pizzaSize: String = ""

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

        var currentCart = cartItemArrayList[position]
        var currentPizza = pizzaItemArrayList[position]
        var count = 0
        pizzaItemNodeArrayList.forEach {
            if(cartItemArrayList[position].pizzaID == it.toString()){
                currentPizza = pizzaItemArrayList[count]
                Log.d("SAMEEEEE", "YES")
                Log.d("cartItemArrayList", "     " + currentCart.pizzaID.toString())
                Log.d("pizzaItemNodeArrayList", it.toString())
                return@forEach
            }
            count++
        }

        Picasso.get().load(currentPizza.img).into(binding.imgPizza)
        binding.tvPizzaTitle.setText(currentPizza.Name)
        binding.tvPrice.setText("Price: $" + currentCart.price.toString())
        binding.tvQ.setText(currentCart.quantity.toString())
        binding.tvSize.setText("Size: " + currentCart.size.toString())

        binding.btnPlusQ.setOnClickListener {
            binding.tvQ.text = (binding.tvQ.text.toString().toInt() + 1).toString()
            setPrice(currentCart.size.toString(), currentPizza, binding)
        }

        binding.btnMinusQ.setOnClickListener {
            if((binding.tvQ.text.toString().toInt()-1) == 0){
                AlertDialog.Builder(context)
                    .setTitle("New quentity is being 0")
                    .setMessage("Are you sure you want to remove this item?")
                    .setIcon(R.drawable.ic_dialog_alert)
                    .setPositiveButton(
                        "Delete",
                        DialogInterface.OnClickListener { dialog, whichButton ->
                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show()
                        })
                    .setNegativeButton("NO", null).show()
            } else if(binding.tvQ.text.toString().toInt() > 0){
                binding.tvQ.text = (binding.tvQ.text.toString().toInt() - 1).toString()
                setPrice(currentCart.size.toString(), currentPizza, binding)
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