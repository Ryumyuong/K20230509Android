package com.example.test13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test13.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data1 = intent.getStringExtra("data1")
        val data2 = intent.getIntExtra("data2",0)

        binding.resultViewText.text = "data1 값 조회 : $data1, data2 값 조회: $data2"

        binding.btn2.setOnClickListener {
            intent.putExtra("result", "후처리 데이터 값")
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}