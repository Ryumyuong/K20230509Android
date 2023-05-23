package com.example.test18_pdtest

import android.app.Application
import com.example.test18_pdtest.retrofit.INetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class MyApplication :Application () {


    lateinit var networkService: INetworkService

    companion object {
            val serviceKey =
                "GWKc8ei%2F%2Fv5E4r5nUQ%2F8w2nKYXGrpkpylgECo0l5n6Zpxi0M2E%2BuPssZksZpDrkZm1q3o0YCJSfA8XXcaarhFQ%3D%3D"
            val numOfRows = "10"
            val pageNo = "1"
            val BASE_URL = "http://apis.data.go.kr/6260000/FoodService/getFoodKr"


            //networkService타입의 변수선언하고 retrofit타입의 변수선언
            var networkService: INetworkService

            val retrofit: Retrofit
                get() = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            //할당하고 networkService에 등록
            init {
                networkService = retrofit.create(INetworkService::class.java)
            }
        }
    }
