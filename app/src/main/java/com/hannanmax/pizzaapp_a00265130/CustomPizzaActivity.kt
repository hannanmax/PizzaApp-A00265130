package com.hannanmax.pizzaapp_a00265130

import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.hannanmax.pizzaapp_a00265130.Data.FirebaseDBHelper
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityCustomPizzaBinding
import java.util.*


class CustomPizzaActivity : AppCompatActivity() {

    lateinit var binding: ActivityCustomPizzaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomPizzaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rgSize.setOnCheckedChangeListener { _, optionId ->
            run {
                when (optionId) {
                    binding.rbSizeS.id -> {
                        setPrice()
                    }
                    binding.rbSizeM.id -> {
                        setPrice()
                    }
                    binding.rbSizeL.id -> {
                        setPrice()
                    }
                }
            }
        }
        
        binding.chkTomatosauce.setOnCheckedChangeListener { _, isChecked ->
            binding.imgTamatosauce.isVisible = isChecked
            setPrice()
        }

        binding.chkCheese.setOnCheckedChangeListener { _, isChecked ->
            binding.imgCheese.isVisible = isChecked
            setPrice()
        }

        binding.chkTomatoes.setOnCheckedChangeListener { _, isChecked ->
            binding.imgTamato.isVisible = isChecked
            setPrice()
        }

        binding.chkJalapenopaper.setOnCheckedChangeListener { _, isChecked ->
            binding.imgJalapenopappers.isVisible = isChecked
            setPrice()
        }

        binding.chkBaconCrumps.setOnCheckedChangeListener { _, isChecked ->
            binding.imgBaconcrumbs.isVisible = isChecked
            setPrice()
        }

        binding.chkBlackOlives.setOnCheckedChangeListener { _, isChecked ->
            binding.imgBlackOlives.isVisible = isChecked
            setPrice()
        }

        binding.chkGreenPapers.setOnCheckedChangeListener { _, isChecked ->
            binding.imgGreenpappers.isVisible = isChecked
            setPrice()
        }

        binding.chkMincedGarlic.setOnCheckedChangeListener { _, isChecked ->
            binding.imgMincedGarlic.isVisible = isChecked
            setPrice()
        }

        binding.chkOnions.setOnCheckedChangeListener { _, isChecked ->
            binding.imgOnions.isVisible = isChecked
            setPrice()
        }

        binding.chkPepperoni.setOnCheckedChangeListener { _, isChecked ->
            binding.imgPepperoni.isVisible = isChecked
            setPrice()
        }

        binding.chkPineapples.setOnCheckedChangeListener { _, isChecked ->
            binding.imgPineapples.isVisible = isChecked
            setPrice()
        }

        binding.chkShrimps.setOnCheckedChangeListener { _, isChecked ->
            binding.imgShrimps.isVisible = isChecked
            setPrice()
        }

        binding.btnPlusQ.setOnClickListener {
            binding.tvQ.text = (binding.tvQ.text.toString().toInt() + 1).toString()
            setPrice()
        }

        binding.btnMinusQ.setOnClickListener {
            if(binding.tvQ.text.toString().toInt() > 1){
                val newQ = (binding.tvQ.text.toString().toInt() - 1)
                binding.tvQ.text = newQ.toString()
                setPrice()
            }
        }

        binding.btnAddToCart.setOnClickListener {
            var pizzaSize = ""
            when (binding.rgSize.checkedRadioButtonId) {
                binding.rbSizeS.id -> pizzaSize = "Small"
                binding.rbSizeM.id -> pizzaSize = "Medium"
                binding.rbSizeL.id -> pizzaSize = "Large"
            }
            val price = binding.tvPrice.text.toString().toDouble()
            val firebaseDBHelper = FirebaseDBHelper()
            firebaseDBHelper.addToCart(applicationContext, "CustomPizza", price, pizzaSize, binding.tvQ.text.toString().toInt())
            val intent = Intent(applicationContext, CartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun setPrice() {
        var sizeTotals = 0.0f
        var ingredienttotal = 0f
        val quantity = binding.tvQ.text.toString().toInt()
        when(binding.rgSize.checkedRadioButtonId){
            binding.rbSizeS.id ->{
                sizeTotals = 2f
            }
            binding.rbSizeM.id ->{
                sizeTotals = 4f
            }
            binding.rbSizeL.id ->{
                sizeTotals = 6f
            }
        }

        if(binding.chkTomatosauce.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkCheese.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkTomatoes.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkJalapenopaper.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkBaconCrumps.isChecked){
            ingredienttotal += 1.98f
        }

        if(binding.chkBlackOlives.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkGreenPapers.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkMincedGarlic.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkOnions.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkPepperoni.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkPineapples.isChecked){
            ingredienttotal += 0.98f
        }

        if(binding.chkShrimps.isChecked){
            ingredienttotal += 2.98f
        }

        var pizzaTotal = 0f
        Log.d("pizzaTotal", pizzaTotal.toString())
        Log.d("ingredienttotal", ingredienttotal.toString())
        Log.d("quantity", quantity.toString())
        Log.d("sizeTotals", sizeTotals.toString())
        if(ingredienttotal == 0f){
            pizzaTotal = sizeTotals * quantity
        } else {
            pizzaTotal = (sizeTotals * quantity) + (ingredienttotal*quantity)
        }
        binding.tvPrice.text = String.format("%.02f", pizzaTotal)
    }
}