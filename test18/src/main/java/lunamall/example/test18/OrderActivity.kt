package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        val networkService = (applicationContext as MyApplication).networkService

        var pageNumber = 1
        var pageSize = 10

        val orderCall = networkService.orderList(username, pageNumber, pageSize)

        orderCall.enqueue(object : Callback<OrderList> {
            override fun onResponse(call: Call<OrderList>, response: Response<OrderList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One item : $item")
                Log.d("lmj", "===========")
                Log.d("lmj", "성공 내용 : ${response.code()}")
                adapter = MyOrderAdapter(item)
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
                    }else {
                        val intent = Intent(this, LoginDetail::class.java)
                        startActivity(intent)
                    }
                }
            }
            true
        }
    }
}