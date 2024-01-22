package com.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.Product
import com.example.test18.databinding.ActivityInsertProductBinding
import com.example.test18.databinding.ActivityUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertProduct : AppCompatActivity() {
    lateinit var binding: ActivityInsertProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "물품 등록"

        binding.category.setOnClickListener{
            val items = arrayOf("전자/기기", "생활가전", "식음료", "운동", "여가문화", "여행", "업무", "VVIP", "웰컴키트")

            // AlertDialog 빌더 생성
            val builder = AlertDialog.Builder(this)
            builder.setTitle("카테고리 선택")
                .setItems(items) { dialog, which ->
                    // 사용자가 목록에서 항목을 선택했을 때 실행되는 코드
                    val selectedValue = items[which]
                    binding.category.text = selectedValue
                    // 선택한 항목에 대한 작업 수행
                    // 예: 토스트 메시지 표시 등
                }

            // AlertDialog 생성 및 표시
            val dialog = builder.create()
            dialog.show()
        }

    }
}