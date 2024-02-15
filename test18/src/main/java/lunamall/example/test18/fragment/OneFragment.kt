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
import lunamall.example.test18.databinding.FragmentOneBinding
import lunamall.example.test18.model.ItemDataList
import lunamall.example.test18.recycler.MyWaitingAdapter
import lunamall.example.test18.recycler.UpdateProductAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OneFragment : Fragment() {

    lateinit var binding: FragmentOneBinding
    lateinit var adapter: MyWaitingAdapter
    lateinit var adapter2: UpdateProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOneBinding.inflate(inflater, container, false)

        val networkService = (context?.applicationContext as MyApplication).networkService

        val reserveListCall = networkService.getMyProduct("전자")

        var userId = ""
        val preferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        reserveListCall.enqueue(object : Callback<ItemDataList> {
            override fun onResponse(call: Call<ItemDataList>, response: Response<ItemDataList>) {
                if (isAdded) {
                    var item = response.body()?.items
                    if(username=="admin") {
                        adapter2 = UpdateProductAdapter(requireContext(), item, username, networkService)
                        binding.oneRecyclerView.adapter = adapter2
                        binding.oneRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                        adapter2.notifyDataSetChanged()
                    } else {
                        adapter = MyWaitingAdapter(requireContext(), item, username, networkService)
                        binding.oneRecyclerView.adapter = adapter
                        binding.oneRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                        adapter.notifyDataSetChanged()
                    }
                }
            }

            override fun onFailure(call: Call<ItemDataList>, t: Throwable) {
                if (isAdded) {
                    Log.d("lmj", "실패 내용 : ${t.message}")
                    call.cancel()
                }
            }

        })

        Log.d("lmj", "----------")

        return binding.root
    }
}