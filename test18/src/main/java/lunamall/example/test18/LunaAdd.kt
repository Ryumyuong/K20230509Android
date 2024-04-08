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

        supportActionBar?.setDisplayShowTitleEnabled(false)

        val networkService = (applicationContext as MyApplication).networkService

        var pageNumber = 1
        var pageSize = 10


            binding.before.setOnClickListener {
                if(!binding.before.background.equals(R.drawable.prev)) {
                    pageNumber = pageNumber.minus(1)
                    binding.next.setImageResource(R.drawable.nexton)
                    val userCall = networkService.userList(pageNumber, pageSize)

                    userCall.enqueue(object : Callback<UserList> {
                        override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                            var item = response.body()?.items
                            adapter = MyLunaAddAdapter(this@LunaAdd, item,networkService)
                            binding.LunaAddRecyclerView.adapter = adapter
                            binding.LunaAddRecyclerView.layoutManager = LinearLayoutManager(this@LunaAdd)
                            binding.LunaAddRecyclerView.addItemDecoration(DividerItemDecoration(this@LunaAdd, LinearLayoutManager.VERTICAL))
                            adapter.notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<UserList>, t: Throwable) {
                            call.cancel()
                        }

                    })
                    if(pageNumber==1) {
                        binding.before.setImageResource(R.drawable.prev)
                    }
                }



            }

        binding.next.setOnClickListener {
            if(!binding.next.background.equals(R.drawable.next)) {
                binding.before.setImageResource(R.drawable.prevon)
                pageNumber = pageNumber.plus(1)

                val userCall = networkService.userList(pageNumber, pageSize)

                userCall.enqueue(object : Callback<UserList> {
                    override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                        var item = response.body()?.items
                        adapter = MyLunaAddAdapter(this@LunaAdd, item,networkService)
                        binding.LunaAddRecyclerView.adapter = adapter
                        binding.LunaAddRecyclerView.layoutManager = LinearLayoutManager(this@LunaAdd)
                        binding.LunaAddRecyclerView.addItemDecoration(DividerItemDecoration(this@LunaAdd, LinearLayoutManager.VERTICAL))
                        if(item?.count()!! <10) {
                            binding.next.setImageResource(R.drawable.next)
                        }
                        adapter.notifyDataSetChanged()

                    }

                    override fun onFailure(call: Call<UserList>, t: Throwable) {
                        call.cancel()
                    }

                })
            }

        }

        val userCall = networkService.userList(pageNumber, pageSize)

        userCall.enqueue(object : Callback<UserList> {
            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                var item = response.body()?.items
                adapter = MyLunaAddAdapter(this@LunaAdd, item,networkService)
                binding.LunaAddRecyclerView.adapter = adapter
                binding.LunaAddRecyclerView.layoutManager = LinearLayoutManager(this@LunaAdd)
                binding.LunaAddRecyclerView.addItemDecoration(DividerItemDecoration(this@LunaAdd, LinearLayoutManager.VERTICAL))
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                call.cancel()
            }

        })

    }
}