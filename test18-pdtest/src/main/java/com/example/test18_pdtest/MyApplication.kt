package com.example.test18_pdtest

import android.app.Application
import com.example.test18_pdtest.retrofit.INetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MyApplication :Application () {


    var networkService: INetworkService

    val retrofit: Retrofit
        get() = Retrofit.Builder()
            //도보여행, 부산맛집정보서비스
            .baseUrl("https://apis.data.go.kr/6260000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    init {
        networkService = retrofit.create(INetworkService::class.java)
    }
}
