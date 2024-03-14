package com.main.lego2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.main.lego2.databinding.ActivityMainBinding
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}

