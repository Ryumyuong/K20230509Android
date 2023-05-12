package com.example.test9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test9.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //values의 strings.xml에서 가져오기
//        binding.textView1.text = getString(R.string.test)
    }
}