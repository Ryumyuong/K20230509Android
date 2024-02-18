package com.main.lego

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.main.lego.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroBinding
    private val SPLASH_TIMEOUT:Long = 2000 //2초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed(Runnable {
            val intent = Intent(this@IntroActivity, DataActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_TIMEOUT)

    }


}