package com.hannanmax.pizzaapp_a00265130

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.carousel.auna.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.hannanmax.pizzaapp_a00265130.Adapter.CustomPizzaListAdapter
import com.hannanmax.pizzaapp_a00265130.Data.FirebaseDBHelper
import com.hannanmax.pizzaapp_a00265130.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val database = Firebase.database
    private val myBannersRef = database.getReference("Offers/Banners")
    private var adapter: CustomPizzaListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llCartbtn.setOnClickListener{
            val intent = Intent(applicationContext, CartActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.llProfilebtn.setOnClickListener{
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // Loading up banners to main screen
        loadBanners()
        loadPizza()
    }

    // Method: Loading up banners from firebase realtime database
    private fun loadBanners() {
        val imageList = ArrayList<SlideModel>()
        myBannersRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                imageList.clear()

                for (ds in snapshot.children) {
                    val imageUrl = ds.child("img").getValue(String::class.java)
                    val imageTitle = ds.child("title").getValue(String::class.java)
                    if (imageUrl != null && imageTitle != null) {
                        imageList.add(SlideModel(imageUrl, imageTitle,true))
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

    // Method: Loading up Pizza from firebase realtime database
    private fun loadPizza() {
        val firebaseDBHelper = FirebaseDBHelper()
        val (pizzaItemNodeArrayList, pizzaItemArrayList) = firebaseDBHelper.getPizzaItemArrayList()
        adapter = CustomPizzaListAdapter(this, pizzaItemNodeArrayList, pizzaItemArrayList)
        binding.lwPizzaList.adapter = adapter
    }
}