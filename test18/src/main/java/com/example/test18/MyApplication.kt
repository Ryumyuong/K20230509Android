package com.example.test18

import android.app.Application
import com.example.test18.retrofit.INetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MyApplication :Application (){
    //networkService타입의 변수선언하고 retrofit타입의 변수선언
    var networkService: INetworkService

    val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    //할당하고 networkService에 등록
    init {
        networkService = retrofit.create(INetworkService::class.java)
    }
}