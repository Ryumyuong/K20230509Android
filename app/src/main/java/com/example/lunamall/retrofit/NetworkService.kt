package com.example.lunamall.retrofit



import com.example.lunamall.model.BlankItem
import com.example.lunamall.model.BlankItemList
import com.example.lunamall.model.ItemData
import com.example.lunamall.model.ItemDataList
import retrofit2.Call
import retrofit2.http.*

interface NetworkService {

    @GET("runa/main")
    fun getMyProduct(@Query("category") s_category: String?): Call<ItemDataList>

    @GET("main/myDining/waitingAll")
    fun getWaitingAll(): Call<ItemDataList>

    @GET("main/myDining/waitingCom")
    fun getWaitingCom(): Call<ItemDataList>

    @GET("main/myDining/waitingCan")
    fun getWaitingCan(): Call<ItemDataList>

    @POST("main/myDining/delete/{w_title}")
    fun  deleteWaitingList(@Path("w_title")w_title:String?):Call<Unit>

    @GET("main/myDining/myBlank")
    fun getMyBlank(@Query("r_username") r_username: String?): Call<BlankItem>

    @GET("main/myDining/blank")
    fun getBlank(): Call<BlankItemList>

    @POST("main/myDining/blank/delete/{b_title}")
    fun  deleteBlankList(@Path("b_title")b_title:String?):Call<Unit>

    @POST("main/myDining/insert")
    fun doInsertReserve(@Body reserve: ItemData?): Call<ItemData>
}