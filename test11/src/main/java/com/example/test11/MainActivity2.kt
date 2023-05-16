package com.example.test11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test11.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar2)

        binding.text1.lineHeight
        binding.text2.lineHeight
    }
}