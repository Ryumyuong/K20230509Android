package com.example.test6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.test6.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {//savedInstanceState -> 객체에 저장
        super.onCreate(savedInstanceState)
            //xml 파일을 불러서 화면 출력, R.layout에 저장되어서 불러온다
        //setContentView(R.layout.activity_main)

        //뷰 바인딩
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var status = 0
        binding.btn1.setOnClickListener {
            if (status == 0) {
                binding.img1.visibility = View.INVISIBLE
                status = 1
            }else {
                binding.img1.visibility = View.VISIBLE
                status = 0
            }
        }

        /*val button1 = findViewById<Button>(R.id.btn1)
        val img1 = findViewById<ImageView>(R.id.img1)
        var status = 0

        button1.setOnClickListener {
            if (status == 0) {
                img1.visibility = View.INVISIBLE
                status = 1
            }else {
                img1.visibility = View.VISIBLE
                status = 0
            }
        }*/
    }
}