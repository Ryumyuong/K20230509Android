package lunamall.example.test18

import android.app.Application
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

    var networkService: INetworkService
    val retrofit: Retrofit
        get() = Retrofit.Builder()
//            .baseUrl("http://172.30.1.61:8088/")
//            .baseUrl("http://192.168.0.175:8088/")
            .baseUrl("https://lunamall.co.kr/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    init {
        networkService = retrofit.create(INetworkService::class.java)
    }
}
