package com.example.test18.retrofit

import com.example.test18.Model.CartList
import com.example.test18.Model.InCart
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.Product
import com.example.test18.Model.ItemDataList
import com.example.test18.Model.Order
import com.example.test18.Model.OrderList
import com.example.test18.Model.UserList
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface INetworkService {
    @GET("luna/main/csrf")
    fun getCsrfToken(): Call<CsrfToken>

    @GET("luna/main/getCategory")
    fun getMyProduct(@Query("category") s_category: String?) : Call<ItemDataList>

    @POST("luna/main/insert")
    fun insertCart(@Header("X-CSRF-TOKEN") csrfToken: String?, @Body cart: InCart?): Call<InCart>

    @GET("luna/main/user")
    fun getUser(@Query("userId") userId: String?) : Call<UserList>

    @GET("luna/main/getCart")
    fun getCart(@Query("userId") userId: String?) : Call<CartList>

    @GET("luna/main/runaList")
    fun runaList(@Query("userId") userId: String?, @Query("pageNumber") pageNumber: Int?, @Query("pageSize") pageSize: Int?) : Call<OrderList>

    @POST("luna/main/notification")
    fun notification(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("userId") userId: String?, @Query("deviceId") deviceId: String?): Call<Unit>

    @POST("luna/main/notificationToken")
    fun notiToken(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("notiToken") notiToken: String?): Call<Unit>

}