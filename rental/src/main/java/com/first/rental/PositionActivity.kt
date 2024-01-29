package com.first.rental

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.first.rental.databinding.ActivityPositionBinding

class PositionActivity : AppCompatActivity() {
    lateinit var binding: ActivityPositionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPositionBinding.inflate(layoutInflater)
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

        binding.guide.text = """블렌디 오시는 길
주차 안내"""

        binding.textView.text = """주차는 총 2곳 이용 가능합니다.

1. 건물 뒷편 주차타워 (승용차, 경차만 이용가능)
2. 블렌디 공터주차장 (모든 차종 이용가능 / 4시간 지원 가능)

[블렌디 건물 주차 타워 안내]
- 주소 : 부산광역시 해운대구 달맞이길 185 

[블렌디 공터주차장 안내]
- 주소 : 부산광역시 해운대구 중동 1491-10

* 주차장은 걸어서 3분 거리에 있으며 주차 타워는 10대, 공터 주차장 20대, 공영 주차장 또는 외부주차장 50대 이용 가능합니다.
"""
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}