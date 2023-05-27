package com.example.test18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test18.Adapter.MyAdapter
import com.example.test18.Model.UserListModel
import com.example.test18.Model.UserModel
import com.example.test18.databinding.ActivityMain3Binding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity3 : AppCompatActivity() {
    lateinit var binding: ActivityMain3Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkService = (applicationContext as MyApplication).networkService

        val userListCall = networkService.doGetUserList("1")
        Log.d("lmj", "url:" + userListCall.request().url().toString())

        userListCall.enqueue(object : Callback<UserListModel> {
            override fun onResponse(call: Call<UserListModel>, response: Response<UserListModel>) {

                val userList = response.body()

                //.......................................

                binding.recyclerView.adapter= MyAdapter(this@MainActivity3, userList?.data)
                binding.recyclerView.addItemDecoration(
                    DividerItemDecoration(this@MainActivity3, LinearLayoutManager.VERTICAL)
                )

                binding.pageView.text=userList?.page
                binding.totalView.text=userList?.total
            }

            override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                call.cancel()
            }
        })

        binding.testButton.setOnClickListener {
//            val call: Call<UserModel> = networkService.test1()//https://reqres.in/users/list?sort=desc

//            val call: Call<UserModel> = networkService.test2("10", "lmj")//https://reqres.in/group/10/users/lmj

//            val call: Call<UserModel> = networkService.test3("age", "lmj")//https://reqres.in/group/users?sort=age&name=lmj

//            val call: Call<UserModel> = networkService.test4(
//                mapOf<String, String>("one" to "hello", "two" to "world"),
//                "lmj"
//            )//https://reqres.in/group/users?one=hello&two=world&name=lmj

//            val call: Call<UserModel> = networkService.test5(
//                UserModel(id="1", firstName = "gildong", lastName = "hong", avatar = "some url"),
//                "lmj"
//            )//https://reqres.in/group/users?name=lmj

//            val call: Call<UserModel> = networkService.test6(
//                "gildong 길동",
//                "hong 홍",
//                "lmj"
//            )//https://reqres.in/user/edit?name=lmj

//            val list: MutableList<String> = ArrayList()
//            list.add("홍길동")
//            list.add("류현진")
//            val call = networkService.test7(list)

//            val call = networkService.test9("http://www.google.com", "lmj")//http://www.google.com/?name=lmj
//
//            Log.d("lmj","url:"+call.request().url().toString())

        }
    }
}