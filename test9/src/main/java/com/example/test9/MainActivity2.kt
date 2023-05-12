package com.example.test9

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test9.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            binding.button.isEnabled = !binding.button.isEnabled
        }

    }
}