package lunamall.example.test18.retrofit

import lunamall.example.test18.model.Cart
import lunamall.example.test18.model.CartList
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.InCart
import lunamall.example.test18.model.InOrder
import lunamall.example.test18.model.ItemDataList
import lunamall.example.test18.model.OrderList
import lunamall.example.test18.model.Product
import lunamall.example.test18.model.User
import lunamall.example.test18.model.UserList
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

    @POST("luna/main/insertCode")
    fun insertCode(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("userId") userId: String?, @Query("code") code: String?): Call<Unit>

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
        @Query("username") username: String?,
    ): Call<Unit>

    @POST("luna/main/notificationLunaToken")
    fun notiLunaToken(
        @Header("X-CSRF-TOKEN") csrfToken: String?,
        @Query("username") username: String?,
        @Query("luna") luna: String,
    ): Call<Unit>

    @POST("luna/main/insertProduct")
    fun insertProduct(@Header("X-CSRF-TOKEN") csrfToken: String?, @Body product: Product?): Call<Unit>

    @POST("luna/main/updateProduct")
    fun updateProduct(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("productName") productName: String?): Call<Unit>

    @POST("luna/main/deleteProduct")
    fun deleteProduct(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("username") username: String?, @Query("productName") productName: String?): Call<Unit>

    @GET("luna/main/userList")
    fun userList(
        @Query("pageNumber") pageNumber: Int?,
        @Query("pageSize") pageSize: Int?
    ): Call<UserList>

    @POST("luna/main/addLuna")
    fun addLuna(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("username") username: String?, @Query("luna") money: Int?, @Body user : User?): Call<Unit>

    @POST("luna/main/minLuna")
    fun minLuna(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("username") username: String?, @Query("luna") moneyValue: Int?, @Body user : User?): Call<Unit>

    @POST("luna/main/newLogin")
    fun newLogin(@Header("X-CSRF-TOKEN") csrfToken: String?, @Body user: User?): Call<User>

    @POST("luna/main/alarm")
    fun alarm(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("username") username: String?): Call<Unit>

    @POST("luna/main/deliver")
    fun deliver(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("username") username: String?, @Query("deliver") deliver: String?): Call<Unit>

    @POST("luna/main/deleteCart")
    fun deleteCart(@Header("X-CSRF-TOKEN") csrfToken: String?, @Query("username") username: String?, @Query("itemName") itemName: String?): Call<Unit>
}