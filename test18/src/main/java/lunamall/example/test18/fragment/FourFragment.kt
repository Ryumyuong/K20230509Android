package lunamall.example.test18.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import lunamall.example.test18.MyApplication
import lunamall.example.test18.databinding.FragmentFourBinding
import lunamall.example.test18.model.ItemDataList
import lunamall.example.test18.recycler.MyWaitingAdapter
import lunamall.example.test18.recycler.UpdateProductAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FourFragment : Fragment() {
    lateinit var binding: FragmentFourBinding
    lateinit var adapter: MyWaitingAdapter
    lateinit var adapter2: UpdateProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFourBinding.inflate(inflater, container, false)

        val networkService = (context?.applicationContext as MyApplication).networkService

        val reserveListCall = networkService.getMyProduct("운동")

        var userId = ""
        val preferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        reserveListCall.enqueue(object : Callback<ItemDataList> {
            override fun onResponse(call: Call<ItemDataList>, response: Response<ItemDataList>) {
                if (isAdded) {
                    var item = response.body()?.items
                    if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경") {
                        adapter2 = UpdateProductAdapter(requireContext(), item, username, networkService)
                        binding.fourRecyclerView.adapter = adapter2
                        binding.fourRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                        adapter2.notifyDataSetChanged()
                    } else {
                        adapter = MyWaitingAdapter(requireContext(), item, username, networkService)
                        binding.fourRecyclerView.adapter = adapter
                        binding.fourRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ItemDataList>, t: Throwable) {
                if (isAdded) {
                    call.cancel()
                }
            }

        })

        return binding.root
    }
}