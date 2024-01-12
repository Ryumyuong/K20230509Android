package com.example.lunamall

import android.app.Application
import com.example.lunamall.retrofit.NetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MyApplication : Application() {

    var networkService: NetworkService
    val retrofit: Retrofit
        get() = Retrofit.Builder()
//            .baseUrl("http://10.100.105.6:8088/")
            .baseUrl("https://lunamall.co.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    init {
        networkService = retrofit.create(NetworkService::class.java)
    }
}