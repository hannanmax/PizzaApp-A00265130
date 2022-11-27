package com.hannanmax.pizzaapp_a00265130

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityMainBinding
import com.hannanmax.pizzaapp_a00265130.databinding.ActivitySignUpBinding
import com.iammert.library.readablebottombar.ReadableBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottombar.setOnItemSelectListener(object : ReadableBottomBar.ItemSelectListener{
            override fun onItemSelected(index: Int) {
                when(index) {
                    0 -> Toast.makeText(applicationContext,"0", Toast.LENGTH_LONG).show()
                    1 -> Toast.makeText(applicationContext,"1", Toast.LENGTH_LONG).show()
                    2 -> Toast.makeText(applicationContext,"2", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}