package com.example.test18.retrofit

import com.example.test18.Model.Cart
import com.example.test18.Model.CartList
import com.example.test18.Model.InCart
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.ItemDataList
import com.example.test18.Model.OrderList
import com.example.test18.Model.UserList
import com.example.test18.Model.InOrder
import com.example.test18.Model.Product
import com.example.test18.Model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface INetworkService {
    @GET("luna/main/csrf")
    fun getCsrfToken(): Call<CsrfToken>

    @GET("luna/main/getCategory")
    fun getMyProduct(@Query("category") s_category: String?): Call<ItemDataList>

    @GET("luna/main/productAll")
    fun getMyProductAll(): Call<ItemDataList>

    @POST("luna/main/insert")
    fun insertCart(@Header("X-CSRF-TOKEN") csrfToken: String?, @Body inCart: InCart?): Call<InCart>

    @GET("luna/main/user")
    fun getUser(@Query("userId") userId: String?): Call<UserList>

    @GET("luna/main/getCart")
    fun getCart(@Query("userId") userId: String?): Call<CartList>

    @GET("luna/main/total")
    fun total(@Query("userId") userId: String?): Call<Cart>


    @GET("luna/main/runaList")
    fun runaList(
        @Query("userId") userId: String?,
        @Query("pageNumber") pageNumber: Int?,
        @Query("pageSize") pageSize: Int?
    ): Call<OrderList>

    @POST("luna/main/order")
    fun order(@Header("X-CSRF-TOKEN") csrfToken: String?, @Body order: InOrder?): Call<InOrder>

    @GET("luna/main/orderList")
    fun orderList(
        @Query("userId") userId: String?,
        @Query("pageNumber") pageNumber: Int?,
        @Query("pageSize") pageSize: Int?
    ): Call<OrderList>

    @POST("luna/main/notificationToken")
    fun notiToken(
        @Header("X-CSRF-TOKEN") csrfToken: String?,
        @Query("notiToken") notiToken: String?
    ): Call<Unit>

    @POST("luna/main/insertProduct")
    fun insertProduct(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("productName") productName: String?): Call<Product>

    @POST("luna/main/updateProduct")
    fun updateProduct(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("productName") productName: String?): Call<Product>

    @POST("luna/main/deleteProduct")
    fun deleteProduct(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("productName") productName: String?): Call<Unit>

    @GET("luna/main/userList")
    fun userList(
        @Query("pageNumber") pageNumber: Int?,
        @Query("pageSize") pageSize: Int?
    ): Call<UserList>

    @POST("luna/main/addLuna")
    fun addLuna(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("username") username: String?, @Body user : User?): Call<Unit>

    @POST("luna/main/minLuna")
    fun minLuna(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("username") username: String?, @Body user : User?): Call<Unit>

    @POST("luna/main/newLogin")
    fun newLogin(@Header("X-CSRF-TOKEN") csrfToken: String?, @Body user: User?): Call<User>

}