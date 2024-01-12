package com.example.test18

import android.app.Application
import com.example.test18.retrofit.INetworkService
import com.google.firebase.FirebaseApp
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

    var networkService: INetworkService
    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("http://172.30.1.95:8088/")/
//            .baseUrl("http://192.168.0.11:8088/")
//            .baseUrl("https://lunamall.co.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    init {
        networkService = retrofit.create(INetworkService::class.java)
    }
}
