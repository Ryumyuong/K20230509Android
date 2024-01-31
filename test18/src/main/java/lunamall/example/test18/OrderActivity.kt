package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isInvisible
import lunamall.example.test18.databinding.ActivityOrderBinding
import lunamall.example.test18.model.OrderList
import lunamall.example.test18.recycler.MyOrderAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrderBinding
    lateinit var adapter: MyOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "주문 목록"

        val bottomNavigationView = binding.bottommenu

        bottomNavigationView.selectedItemId = R.id.third_tab

        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        val networkService = (applicationContext as MyApplication).networkService

        var pageNumber = 1
        var pageSize = 10

        binding.before.isInvisible = true

        binding.before.setOnClickListener {
            pageNumber = pageNumber.minus(1)
            if (pageNumber != 1) {
                val orderCall = networkService.orderList(username, pageNumber, pageSize)

                orderCall.enqueue(object : Callback<OrderList> {
                    override fun onResponse(call: Call<OrderList>, response: Response<OrderList>) {
                        var item = response.body()?.items
                        Log.d("lmj", "-------")
                        Log.d("lmj", "One item : $item")
                        Log.d("lmj", "===========")
                        Log.d("lmj", "성공 내용 : ${response.code()}")
                        adapter = MyOrderAdapter(this@OrderActivity, item, networkService)
                        binding.OrderRecyclerView.adapter = adapter
                        binding.next.isInvisible = false
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<OrderList>, t: Throwable) {
                        Log.d("lmj", "실패 내용 : ${t.message}")
                        call.cancel()
                    }

                })
            } else {
                val orderCall = networkService.orderList(username, pageNumber, pageSize)

                orderCall.enqueue(object : Callback<OrderList> {
                    override fun onResponse(call: Call<OrderList>, response: Response<OrderList>) {
                        var item = response.body()?.items
                        Log.d("lmj", "-------")
                        Log.d("lmj", "One item : $item")
                        Log.d("lmj", "===========")
                        Log.d("lmj", "성공 내용 : ${response.code()}")
                        adapter = MyOrderAdapter(this@OrderActivity, item, networkService)
                        binding.OrderRecyclerView.adapter = adapter
                        binding.before.isInvisible = true
                        binding.next.isInvisible = false
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<OrderList>, t: Throwable) {
                        Log.d("lmj", "실패 내용 : ${t.message}")
                        call.cancel()
                    }

                })

            }
        }

        binding.next.setOnClickListener {
            pageNumber = pageNumber.plus(1)
            binding.before.isInvisible = false

            val orderCall = networkService.orderList(username, pageNumber, pageSize)

            orderCall.enqueue(object : Callback<OrderList> {
                override fun onResponse(call: Call<OrderList>, response: Response<OrderList>) {
                    var item = response.body()?.items
                    Log.d("lmj", "-------")
                    Log.d("lmj", "One item : $item")
                    Log.d("lmj", "===========")
                    Log.d("lmj", "성공 내용 : ${response.code()}")
                    adapter = MyOrderAdapter(this@OrderActivity, item, networkService)
                    binding.OrderRecyclerView.adapter = adapter
                    if(item?.count()!! <10) {
                        binding.next.isInvisible = true
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<OrderList>, t: Throwable) {
                    Log.d("lmj", "실패 내용 : ${t.message}")
                    call.cancel()
                }

            })
        }

        val orderCall = networkService.orderList(username, pageNumber, pageSize)

        orderCall.enqueue(object : Callback<OrderList> {
            override fun onResponse(call: Call<OrderList>, response: Response<OrderList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One item : $item")
                Log.d("lmj", "===========")
                Log.d("lmj", "성공 내용 : ${response.code()}")
                adapter = MyOrderAdapter(this@OrderActivity, item, networkService)
                binding.OrderRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<OrderList>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })

        binding.bottommenu.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.first_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }
                R.id.second_tab -> {
                    Log.d("lmj", "username==$username==")
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.third_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, OrderActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.fourth_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else if(username.equals("admin")){
                        val intent = Intent(this, LoginAdmin::class.java)
                        startActivity(intent)
                    } else {
                        val intent = Intent(this, LoginDetail::class.java)
                        startActivity(intent)
                    }
                }
            }
            true
        }
    }
}