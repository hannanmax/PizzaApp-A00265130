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
import com.hannanmax.pizzaapp_a00265130.Data.OrdersItemList
import com.hannanmax.pizzaapp_a00265130.Data.PizzaItemList
import com.hannanmax.pizzaapp_a00265130.R
import com.hannanmax.pizzaapp_a00265130.databinding.CustomCartItemListLayoutBinding
import com.hannanmax.pizzaapp_a00265130.databinding.CustomOrderedItemListLayoutBinding
import com.squareup.picasso.Picasso

class CustomOrderesListAdapter(
    private val context: Context,
    private val orderItemArrayList: ArrayList<OrdersItemList>) : BaseAdapter() {

    override fun getCount(): Int {
        return orderItemArrayList.size
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
            CustomOrderedItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            CustomOrderedItemListLayoutBinding.bind(convertView)
        }

        val currentOrder = orderItemArrayList[position]

        binding.tvOrderdate.text = "OrderDate: " + currentOrder.order_date
        binding.tvPrice.text = "Price: $" + currentOrder.cartTotalWithTax.toString()
        binding.tvPhoneNo.text = currentOrder.phoneno.toString()
        var address = currentOrder.houseno.toString() + ", " + currentOrder.streetname.toString() + ", " + currentOrder.city.toString() + ", " + currentOrder.province.toString() + ", " + currentOrder.postalcode.toString();
        binding.tvShippingTo.text = address

        return binding.root
    }
}