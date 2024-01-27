package com.first.rental

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.first.rental.databinding.ActivityPlaceBinding

class PlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "로고"

        binding.tour.setOnClickListener {
            val intent = Intent(this, TourActivity::class.java)
            startActivity(intent)
        }

        binding.place.setOnClickListener {
            val intent = Intent(this, PlaceActivity::class.java)
            startActivity(intent)
        }

        binding.order.setOnClickListener {
            val intent = Intent(this, OrderActivity::class.java)
            startActivity(intent)
        }

        binding.position.setOnClickListener {
            val intent = Intent(this, PositionActivity::class.java)
            startActivity(intent)
        }
    }
}