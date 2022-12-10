package com.hannanmax.pizzaapp_a00265130.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hannanmax.pizzaapp_a00265130.Data.FirebaseDBHelper
import com.hannanmax.pizzaapp_a00265130.Data.PizzaItemList
import com.hannanmax.pizzaapp_a00265130.databinding.CustomPizzaItemListLayoutBinding
import com.squareup.picasso.Picasso


class CustomPizzaListAdapter(private val context: Context, private val pizzaItemNodeList: ArrayList<String>, private val pizzaItemList: ArrayList<PizzaItemList>) : BaseAdapter() {

    var pizzaSize: String = ""

    override fun getCount(): Int {
        return pizzaItemList.size
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
            CustomPizzaItemListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            CustomPizzaItemListLayoutBinding.bind(convertView)
        }

        val current = pizzaItemList[position]
        Picasso.get().load(current.img).into(binding.imgPizza)
        binding.tvPizzaTitle.setText(current.Name)
        binding.tvPizzaDesc.setText(current.Desc)
        setPrice(binding.rgSize.checkedRadioButtonId, position, binding)

        binding.rgSize.setOnCheckedChangeListener { _, checkedId ->
            setPrice(checkedId, position, binding)
        }

        binding.btnPlusQ.setOnClickListener {
            binding.tvQ.text = (binding.tvQ.text.toString().toInt() + 1).toString()
            setPrice(binding.rgSize.checkedRadioButtonId, position, binding)
        }

        binding.btnMinusQ.setOnClickListener {
            if(binding.tvQ.text.toString().toInt() > 0){
                binding.tvQ.text = (binding.tvQ.text.toString().toInt() - 1).toString()
                setPrice(binding.rgSize.checkedRadioButtonId, position, binding)
            }
        }

        binding.btnAddToCart.setOnClickListener {
            if(binding.tvQ.text.toString().toInt() > 0){
                when (binding.rgSize.checkedRadioButtonId) {
                    binding.rgSmall.id -> pizzaSize = "Small"
                    binding.rgMedium.id -> pizzaSize = "Medium"
                    binding.rgLarge.id -> pizzaSize = "Large"
                }
                val price = binding.tvPrice.text.split("Price $")[1]
                val firebaseDBHelper = FirebaseDBHelper()
                firebaseDBHelper.addToCart(context, pizzaItemNodeList[position], price, pizzaSize, binding.tvQ.text.toString().toInt())
            } else {
                Toast.makeText(context,"Please add quantity",Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun setPrice(checkedId: Int, position: Int, binding: CustomPizzaItemListLayoutBinding) {
        val current = pizzaItemList[position]
        val price: Float
        when (checkedId) {
            binding.rgSmall.id -> {
                price = binding.tvQ.text.toString().toInt() * current.smPrice!!
                binding.tvPrice.text = "Price $$price"
            }
            binding.rgMedium.id -> {
                price = binding.tvQ.text.toString().toInt() * current.mPrice!!
                binding.tvPrice.text = "Price $$price"
            }
            binding.rgLarge.id -> {
                price = binding.tvQ.text.toString().toInt() * current.lgPrice!!
                binding.tvPrice.text = "Price $$price"
            }
            else -> {
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show()
            }
        }
    }
}