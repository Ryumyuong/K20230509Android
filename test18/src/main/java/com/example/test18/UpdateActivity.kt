package com.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.ItemDataList
import com.example.test18.databinding.ActivityMainBinding
import com.example.test18.databinding.ActivityUpdateBinding
import com.example.test18.fragment.EightFragment
import com.example.test18.fragment.FiveFragment
import com.example.test18.fragment.FourFragment
import com.example.test18.fragment.NineFragment
import com.example.test18.fragment.OneFragment
import com.example.test18.fragment.SevenFragment
import com.example.test18.fragment.SixFragment
import com.example.test18.fragment.ThreeFragment
import com.example.test18.fragment.TwoFragment
import com.example.test18.recycler.MyProductAllAdapter
import com.example.test18.recycler.MyWaitingAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.messaging.FirebaseMessaging
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