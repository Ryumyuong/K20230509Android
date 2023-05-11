package com.example.test6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {//savedInstanceState -> 객체에 저장
        super.onCreate(savedInstanceState)
            //xml 파일을 불러서 화면 출력, R.layout에 저장되어서 불러온다
        setContentView(R.layout.activity_main)
    }
}