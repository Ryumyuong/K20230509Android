package com.example.test18_pdtest.retrofit

import com.example.test18_pdtest.Model.UserListModel
import com.example.test18_pdtest.Model.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface INetworkService {
//    http://apis.data.go.kr/6260000/FoodService/getFoodKr
//    ?serviceKey=인증키&numOfRows=10&pageNo=1 와 동일

    @GET("/FoodService/getFoodKr")
    fun getList(
        @Query("serviceKey") serviceKey: String?,
        @Query("pageNo") pageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("resultType") resultType : String
    ): retrofit2.Call<UserListModel>
    fun doGetUserList(@Query("page") page: String): Call<UserListModel>
    @GET
    fun getAvatarImage(@Url url: String): Call<ResponseBody>

//    @GET("users/list?sort=desc")
    @GET("6260000/FoodService/getFoodKr/1")
    fun test1(): Call<UserModel>
}