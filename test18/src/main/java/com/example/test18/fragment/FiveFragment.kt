package com.example.test18.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test18.Model.ItemDataList
import com.example.test18.MyApplication
import com.example.test18.databinding.FragmentFiveBinding
import com.example.test18.recycler.MyWaitingAdapter
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FiveFragment : Fragment() {
    lateinit var binding: FragmentFiveBinding
    lateinit var adapter: MyWaitingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFiveBinding.inflate(inflater, container, false)

        val networkService = (context?.applicationContext as MyApplication).networkService

        val reserveListCall = networkService.getMyProduct("여가문화")

        var userId = ""
        val preferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        reserveListCall.enqueue(object : Callback<ItemDataList> {
            override fun onResponse(call: Call<ItemDataList>, response: Response<ItemDataList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One item : $item")
                Log.d("lmj", "===========")
                Log.d("lmj", "실패 내용 : ${response.code()}")
                adapter = MyWaitingAdapter(requireContext(),item,username, networkService)


                binding.fiveRecyclerView.adapter = adapter
                binding.fiveRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ItemDataList>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })

        Log.d("lmj", "----------")

        return binding.root
    }
}