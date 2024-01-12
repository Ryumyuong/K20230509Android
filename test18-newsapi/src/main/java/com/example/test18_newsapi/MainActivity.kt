package com.example.test18_newsapi

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.test18_newsapi.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var webView: WebView? = null
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        webView = binding.webView

        // WebView 설정
        val webSettings:WebSettings = webView!!.getSettings()
        webSettings.javaScriptEnabled = true // JavaScript 활성화

        // 웹페이지 로드
        webView!!.loadUrl("https://lunamall.co.kr/runa/main?category=")
    }
}