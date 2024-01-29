package com.first.rental

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.first.rental.databinding.ActivityPlaceBinding
import com.first.rental.recycler.ImagePagerAdapter

class PlaceActivity : AppCompatActivity() {
    lateinit var binding: ActivityPlaceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val viewPager: ViewPager2 = findViewById(R.id.Viewpager)  // ViewPager2 ID로 변경
//        val imageList = listOf(
//            R.drawable.image1,  // 각 이미지에 대한 리소스 ID로 변경
//            R.drawable.image2,
//            R.drawable.image3
//        )
//
//        val adapter = ImagePagerAdapter(imageList)
//        viewPager.adapter = adapter


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

        binding.point.text = """point 01 ) 최대 70인 수용 가능한 여유로운 공간!
point 02 ) 빔프로젝터와 음향시설, 마이크 등 준비 되어 있어요!
Point 03 ) 뷔페 및 코스, 단품 요리 등 예산에 맞게 퀄리티 높은 음식을 즐길 수 있어요!
"""

        binding.text.text = """대관 이용 시간은 기본 3시간이며 추가 이용 시 금액이 추가됩니다. (준비 시간 포함)
행사 목적에 따라 견적 금액이 상이하니 상담 후 계약 진행해 주세요."""

        binding.textView.text = "지금 바로 대관 견적 알아보러 가기"

        binding.textView.paintFlags = binding.textView.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.textView.setOnClickListener {
            val url = "https://i.listovey.com/c/rlatkanwkd76"

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