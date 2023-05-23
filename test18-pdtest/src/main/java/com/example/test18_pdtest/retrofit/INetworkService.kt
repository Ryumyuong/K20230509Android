package com.example.test18_pdtest.retrofit

import com.example.test18_pdtest.Model.UserListModel
import com.example.test18_pdtest.Model.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface INetworkService {
    @GET("6260000/FoodService/getFoodKr")
//    http://apis.data.go.kr/6260000/FoodService/getFoodKr?serviceKey=GWKc8ei%2F%2Fv5E4r5nUQ%2F8w2nKYXGrpkpylgECo0l5n6Zpxi0M2E%2BuPssZksZpDrkZm1q3o0YCJSfA8XXcaarhFQ%3D%3D&numOfRows=10&pageNo=1 와 동일
    fun doGetUserList(@Query("page") page: String): Call<UserListModel>
    @GET
    fun getAvatarImage(@Url url: String): Call<ResponseBody>

//    @GET("users/list?sort=desc")
    @GET("6260000/FoodService/getFoodKr/1")
    fun test1(): Call<UserModel>
}