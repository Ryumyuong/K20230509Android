package com.example.test12

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity


class IntroMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro_main)

        Handler().postDelayed({
            val intent = Intent(this@IntroMainActivity, MainActivity389::class.java)
            startActivity(intent)
            finish()
        }, 3000)


    }
}