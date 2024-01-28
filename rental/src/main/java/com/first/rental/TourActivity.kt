package com.first.rental

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.first.rental.databinding.ActivityTourBinding

class TourActivity : AppCompatActivity() {
    lateinit var binding: ActivityTourBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTourBinding.inflate(layoutInflater)
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

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
            }
        }

        binding.text.text = """오션뷰를 바라보며 즐길 수 있는
우리만의 프라이빗한 시간들.
                                
각종 모임의 성격에 맞게 공간 준비가 되어 있습니다.
인원 또는 예산을 말씀해 주시면 커스텀 도와드릴게요.
맛있는 음식과 다양한 주류, 다채로운 서비스를 누려보세요"""
    }
}