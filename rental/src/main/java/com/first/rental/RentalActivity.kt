package com.first.rental

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.first.rental.databinding.ActivityRentalBinding

class RentalActivity : AppCompatActivity() {
    lateinit var binding: ActivityRentalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRentalBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.logo.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.rental.setOnClickListener {
            val intent = Intent(this, RentalActivity::class.java)
            startActivity(intent)
        }

        binding.buy.setOnClickListener {
            val url = "http://www.lunamall.co.kr"

            if (!url.isNullOrBlank()) {
                openLink(url)
            }
        }

    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}