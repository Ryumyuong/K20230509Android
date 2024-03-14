package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.databinding.ActivityOrderBinding
import lunamall.example.test18.model.OrderList
import lunamall.example.test18.recycler.MyOrderAdapter
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderActivity : AppCompatActivity() {
    lateinit var binding: ActivityOrderBinding
    lateinit var adapter: MyOrderAdapter
    lateinit var layoutManager: LinearLayoutManager
    var loading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)


        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        val networkService = (applicationContext as MyApplication).networkService

        var pageNumber = 1
        var pageSize = 10


        layoutManager = LinearLayoutManager(this)
        binding.OrderRecyclerView.layoutManager = layoutManager

        loadOrders(username,pageNumber,pageSize,networkService)

        binding.OrderRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)


                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (!loading && totalItemCount <= (lastVisibleItem + 1)) {
                    loading = true // 중복 호출 방지
                    pageNumber++
                    loadOrders(username,pageNumber,pageSize,networkService)

                }

            }

        })

        binding.home.setOnClickListener{
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.cart.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경") {
                val intent = Intent(this, InsertProduct::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }

        binding.list.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경"){
                val intent = Intent(this, UserListActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LunaActivity::class.java)
                startActivity(intent)
            }
        }

        binding.profile.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경"){
                val intent = Intent(this, LoginAdmin::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginDetail::class.java)
                startActivity(intent)
            }
        }

    }

    private fun loadOrders(username: String?, pageNumber: Int, pageSize: Int, networkService: INetworkService) {
        val orderCall = networkService.orderList(username, pageNumber, pageSize)

        orderCall.enqueue(object : Callback<OrderList> {
            override fun onResponse(call: Call<OrderList>, response: Response<OrderList>) {
                var item = response.body()?.items
                item?.let {
                    if (!::adapter.isInitialized) {
                        adapter = MyOrderAdapter(this@OrderActivity, it, networkService)
                        binding.OrderRecyclerView.adapter = adapter
                    } else {
                        adapter.addItems(it)
                        adapter.notifyDataSetChanged()
                    }
                }

//                adapter = MyOrderAdapter(this@OrderActivity, item, networkService)
//                binding.OrderRecyclerView.adapter = adapter
//                adapter.notifyDataSetChanged()
                loading = false
            }

            override fun onFailure(call: Call<OrderList>, t: Throwable) {
                call.cancel()
                loading = false
            }

        })
    }


}