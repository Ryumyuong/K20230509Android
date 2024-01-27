package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import lunamall.example.test18.databinding.ActivityUpdateBinding
import lunamall.example.test18.model.ItemDataList
import lunamall.example.test18.recycler.MyProductAllAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateBinding
    lateinit var adapter: MyProductAllAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "루나몰"

        val networkService = (applicationContext as MyApplication).networkService

        val reserveListCall = networkService.getMyProductAll()

        reserveListCall.enqueue(object : Callback<ItemDataList> {
            override fun onResponse(call: Call<ItemDataList>, response: Response<ItemDataList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One item : $item")
                Log.d("lmj", "===========")
                Log.d("lmj", "실패 내용 : ${response.code()}")
                adapter = MyProductAllAdapter(this@UpdateActivity,item,networkService)

                binding.productRecyclerView.adapter = adapter
                binding.productRecyclerView.addItemDecoration(DividerItemDecoration(this@UpdateActivity, LinearLayoutManager.VERTICAL))
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ItemDataList>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })

        binding.bottommenu.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.first_tab -> {
                        val intent = Intent(this, UpdateActivity::class.java)
                        startActivity(intent)
                }
                R.id.second_tab -> {
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                }
                R.id.third_tab -> {
                        val intent = Intent(this, OrderActivity::class.java)
                        startActivity(intent)
                }
                R.id.fourth_tab -> {
                        val intent = Intent(this, LoginAdmin::class.java)
                        startActivity(intent)
                }
            }
            true
        }
    }
}