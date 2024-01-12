package com.example.test_pdtest.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.test_pdtest.Model.ItemDataList
import com.example.test_pdtest.MyApplication
import com.example.test_pdtest.databinding.FragmentOneBinding
import com.example.test_pdtest.recycler.MyWaitingAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OneFragment : Fragment() {

    lateinit var binding: FragmentOneBinding
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