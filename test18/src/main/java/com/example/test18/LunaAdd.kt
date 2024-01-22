package com.example.test18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test18.Model.UserList
import com.example.test18.databinding.ActivityLoginAdminBinding
import com.example.test18.databinding.ActivityLunaAddBinding
import com.example.test18.recycler.MyLunaAdapter
import com.example.test18.recycler.MyLunaAddAdapter
import com.example.test18.recycler.MyUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LunaAdd : AppCompatActivity() {
    lateinit var binding: ActivityLunaAddBinding
    lateinit var adapter: MyLunaAddAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLunaAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.Toolbar.title = "루나 추가"

        val networkService = (applicationContext as MyApplication).networkService

        var pageNumber = 1
        var pageSize = 10000

        val userCall = networkService.userList(pageNumber, pageSize)

        userCall.enqueue(object : Callback<UserList> {
            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One item : $item")
                Log.d("lmj", "===========")
                adapter = MyLunaAddAdapter(this@LunaAdd, item,networkService)
                binding.LunaAddRecyclerView.adapter = adapter
                binding.LunaAddRecyclerView.layoutManager = LinearLayoutManager(this@LunaAdd)
                binding.LunaAddRecyclerView.addItemDecoration(DividerItemDecoration(this@LunaAdd, LinearLayoutManager.VERTICAL))
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })


    }
}