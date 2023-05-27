package com.example.test_pdtest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_pdtest.Adapter.MyAdapter
import com.example.test_pdtest.Model.UserListModel
import com.example.test_pdtest.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val serviceKey = "GWKc8ei//v5E4r5nUQ/8w2nKYXGrpkpylgECo0l5n6Zpxi0M2E+uPssZksZpDrkZm1q3o0YCJSfA8XXcaarhFQ=="
        val resultType = "json"
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkService = (applicationContext as MyApplication).networkService


        var userListCall = networkService.getList(serviceKey,10,1,resultType)

        Log.d("lmj", "url:" + userListCall.request().url().toString())

        userListCall.enqueue(object : Callback<UserListModel> {
            override fun onResponse(call: Call<UserListModel>, response: Response<UserListModel>) {

                val userList = response.body()

                //.......................................

                binding.recyclerView.adapter = MyAdapter(this@MainActivity, userList?.getFestivalKr?.item)
                binding.recyclerView.addItemDecoration(
                    DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
                )

//                binding.pageView.text = userList?.page
//                binding.totalView.text = userList?.total
            }

            override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                call.cancel()
            }
        })
        var count = 1
        binding.fbtnBefore.visibility = View.INVISIBLE

        binding.fbtnBefore.setOnClickListener{
            binding.fbtnAfter.visibility = View.VISIBLE

                userListCall = networkService.getList(serviceKey, 10, count - 1, resultType)

                userListCall.enqueue(object : Callback<UserListModel> {
                    override fun onResponse(
                        call: Call<UserListModel>,
                        response: Response<UserListModel>
                    ) {
                        val userList = response.body()

                        //.......................................

                        binding.recyclerView.adapter =
                            MyAdapter(this@MainActivity, userList?.getFestivalKr?.item)
                        binding.recyclerView.addItemDecoration(
                            DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
                        )
                    }

                    override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                        call.cancel()
                    }
                })
                count -= 1
            if(count == 1)
                binding.fbtnBefore.visibility = View.INVISIBLE
            else
                binding.fbtnBefore.visibility = View.VISIBLE
        }

        binding.fbtnAfter.setOnClickListener{
            binding.fbtnBefore.visibility = View.VISIBLE

            userListCall = networkService.getList(serviceKey,10,count+1,resultType)

             userListCall.enqueue(object : Callback<UserListModel> {
                override fun onResponse(call: Call<UserListModel>, response: Response<UserListModel>) {

                    val userList = response.body()

                    //.......................................

                    binding.recyclerView.adapter = MyAdapter(this@MainActivity, userList?.getFestivalKr?.item)
                    binding.recyclerView.addItemDecoration(
                        DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL)
                    )

                    count += 1
                    if(userList?.getFestivalKr?.item?.size != 10)
                        binding.fbtnAfter.visibility = View.INVISIBLE

                }

                override fun onFailure(call: Call<UserListModel>, t: Throwable) {
                    call.cancel()
                }
            })




        }
    }
}