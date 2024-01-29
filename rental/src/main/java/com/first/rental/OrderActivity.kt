package com.first.rental

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import com.first.rental.databinding.ActivityOrderBinding

class OrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrderBinding

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
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

        binding.reservation.text = """예약 절차

1. 예약 가능 확인 (전화 또는 카톡)
예약 전 원하시는 시간과 날짜에 예약 가능 여부를 확인해 주세요.

2. 견적 확인
모임 및 인원에 따라 상담을 진행하세요!

3. 예약 확정
상담 견적 확인 후 예약금을 입금해 주세요!"""

        binding.Refund.text = """Refund &
Cancelation
        """

        binding.refund.text = """취소 / 변경 / 환불 규정

이용일 3개월 전 100% 환불
이용일 2개월 전 50% 환불
이용일 1개월 전 30% 환불
이용일 당일 환불 불가"""

        binding.ans1.text = """1. 원하시는 날짜와 인원을 먼저 말씀해 주세요.
실시간 가능 여부 확인, 문자, 카톡, 전화 등 어떤 방식으로도 가능합니다

2. 모임 특성 및 인원에 따라 상담을 진행해 보세요.
3. 견적 이후 예약금 입금이 확인 되면 예약 확정!"""

        binding.ans2.text = """블렌디는 타 업체와 달리 최소 보증 인원을 진행하지 않습니다.

인원의 경우 견적 주신 인원을 기준으로 진행하며
최소 일주일 전 인원 확정을 여쭤봅니다만 
인원 추가는 가능하나 감소는 어려운 점 참고해 주세요.

*인원 확정 인원 변동이 크게 줄어든다면, 대관비 등 추가 발생할 수 있습니다."""

        binding.ans3.text = """대관은 기본 3시간으로 안내해 드리고 있으며 
준비 및 정리는 대관 시간에 포함되어 있습니다.

3시간 이상 이용하고 싶으시다면 시간 추가를 하시면 됩니다.
"""

        binding.ans4.text = """반짝거리는 오션뷰를 보고 싶으시다면 11시부터 2시를 추천드리며
아늑한 불멍 분위기를 느끼고 싶다면 5시부터 8시를 추천드려요."""

        binding.ans5.text = """블렌디는 빔프로젝터, 마이크 등 전문 음향 시설을 갖추고 있습니다.

파일에 쓸 영상 및 음악이 있다면 타임테이블과 함께 행사 2-3일전 메일로 보내주세요!

메일 주소 2hanb10@naver.com."""

        binding.ans6.text = """외부 음식의 경우 식기 세팅비가 추가 됩니다.

와인 파티를 제외한 행사일 경우 주류 반입 (콜키지) 비용이 발생합니다.
만약 콜키지 병 수가 많다면 인원 잔 차지 비용이 더 저렴할 수 있으니 참고해 주세요.

세팅비 – 2만 원
콜키지 비용 - 병 당 3만 원, 
잔 차지 비용 1인 1만 원"""
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(intent)
    }
}