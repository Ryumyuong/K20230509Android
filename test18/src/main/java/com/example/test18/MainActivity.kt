package com.example.test18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.test18.Model.UserListModel
import com.example.test18.Model.UserModel
import com.example.test18.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //다운캐스팅해서 해당 객체 생성
        val networkService = (applicationContext as MyApplication).networkService

        // doGetUserList - 추상함수
        val userListcall = networkService.doGetUserList("2")
        // @Get("users/list?sort=desc") 사용해보기

        binding.btn1test1.setOnClickListener {
            val test1 = networkService.test1()
            test1.enqueue(object : Callback<UserModel> {
                override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                    val userModelSample = response.body()
                    Log.d("lmj","test1()호출 예제, 값 조회 : ${userModelSample}")
                }

                override fun onFailure(call: Call<UserModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }

        //사용하기
        userListcall.enqueue(
            object : Callback<UserListModel> {
                // onResponse 서버로 부터 응답을 성공했을 때 수행하는 함수
                // onFailure 실패했을 때 수행하는 함수
                override fun onResponse(
                    call: Call<UserListModel>,
                    response: Response<UserListModel>
                ) {
                    val userList = response.body()
                    Log.d("lmj","userList의 값 : ${userList}")
                }

                override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
    }
}