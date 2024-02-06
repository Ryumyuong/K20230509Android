package lunamall.example.test18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import lunamall.example.test18.databinding.ActivityLunaAddBinding
import lunamall.example.test18.model.UserList
import lunamall.example.test18.recycler.MyLunaAddAdapter
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
        var pageSize = 10

        binding.before.isInvisible = true

            binding.before.setOnClickListener {
                pageNumber = pageNumber.minus(1)
                if(pageNumber!=1) {
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
                            binding.next.isInvisible = false
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<UserList>, t: Throwable) {
                            Log.d("lmj", "실패 내용 : ${t.message}")
                            call.cancel()
                        }

                    })
                } else {
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
                            binding.next.isInvisible = false
                            binding.before.isInvisible = true
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<UserList>, t: Throwable) {
                            Log.d("lmj", "실패 내용 : ${t.message}")
                            call.cancel()
                        }

                    })

                }


            }

        binding.next.setOnClickListener {
            binding.before.isInvisible = false
            pageNumber = pageNumber.plus(1)

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
                    if(item?.count()!! <10) {
                        binding.next.isInvisible = true
                    }
                    adapter.notifyDataSetChanged()

                }

                override fun onFailure(call: Call<UserList>, t: Throwable) {
                    Log.d("lmj", "실패 내용 : ${t.message}")
                    call.cancel()
                }

            })
        }

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