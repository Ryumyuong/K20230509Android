package com.example.test_pdtest.retrofit

import com.example.test_pdtest.Model.UserListModel
import com.example.test_pdtest.Model.UserModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NetworkService {
    @GET("FestivalService/getFestivalKr")
    fun getList(
        @Query("serviceKey") serviceKey: String?,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("resultType") resultType : String
    ): retrofit2.Call<UserListModel>

}