package com.example.test18

import android.content.Intent
import android.os.Bundle
import android.os.Handler;
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.test18.Model.CsrfToken
import com.example.test18.databinding.ActivityIntroBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IntroActivity : AppCompatActivity() {
    lateinit var binding: ActivityIntroBinding
    private val SPLASH_TIMEOUT:Long = 2000 //2초

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler().postDelayed(Runnable {
            // 인트로 화면 표시 후 메인 액티비티로 이동
            val intent = Intent(this@IntroActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }, SPLASH_TIMEOUT)

    }


}