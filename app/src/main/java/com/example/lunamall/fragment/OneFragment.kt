package com.example.lunamall.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.lunamall.MyApplication
import com.example.lunamall.databinding.FragmentOneBinding
import com.example.lunamall.databinding.FragmentOneWaitingBinding
import com.example.lunamall.databinding.FragmentThreeWaitingBinding
import com.example.lunamall.databinding.FragmentTwoWaitingBinding
import com.example.lunamall.model.ItemDataList
import com.example.lunamall.recycler.MyWaitingAdapter

import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OneFragment : Fragment() {

    lateinit var binding: FragmentOneBinding
    lateinit var bindingOne: FragmentOneWaitingBinding
    lateinit var bindingTwo: FragmentTwoWaitingBinding
    lateinit var bindingThree: FragmentThreeWaitingBinding
    lateinit var adapter: MyWaitingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneBinding.inflate(inflater, container, false)

        val networkService = (context?.applicationContext as MyApplication).networkService
        val reserveListCall = networkService.getMyProduct("전자")
        Log.d("lmj", "-------")

        reserveListCall.enqueue(object : Callback<ItemDataList> {
            override fun onResponse(call: Call<ItemDataList>, response: Response<ItemDataList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One item : $item")
                Log.d("lmj", "===========")
                adapter = MyWaitingAdapter(OneFragment(), item)


                binding.oneRecyclerView.adapter = adapter
                binding.oneRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ItemDataList>, t: Throwable) {
                call.cancel()
            }

        })

        return binding.root
    }
}