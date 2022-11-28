package com.hannanmax.pizzaapp_a00265130

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.carousel.auna.interfaces.ItemClickListener
import com.carousel.auna.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.hannanmax.pizzaapp_a00265130.Model.Banner
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityMainBinding
import com.hannanmax.pizzaapp_a00265130.databinding.ActivitySignUpBinding
import com.iammert.library.readablebottombar.ReadableBottomBar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val database = Firebase.database
    val myBannersRef = database.getReference("Offers/Banners")

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

        // Settings up banners to main screen
        setupBanners()
    }

    // Method: setting up banners from firebase realtime database
    private fun setupBanners() {
        val imageList = ArrayList<SlideModel>()
        myBannersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                imageList.clear()
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue()
                Log.d(TAG, "Value is: " + value.toString())

                for (ds in snapshot.getChildren()) {
                    val imageUrl = ds.child("img").getValue(String::class.java)
                    val imageTitle = ds.child("title").getValue(String::class.java)
                    if (imageUrl != null && imageTitle != null) {
                        imageList.add(SlideModel(imageUrl,imageTitle,true))
                    }
                }
                binding.banner.setImageList(imageList) //centerCrop for all images
                binding.banner.startSliding(3000)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }
}