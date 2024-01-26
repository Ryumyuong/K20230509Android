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
import lunamall.example.test18.databinding.FragmentNineBinding
import lunamall.example.test18.model.ItemDataList
import lunamall.example.test18.recycler.MyWaitingsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NineFragment : Fragment() {
    lateinit var binding: FragmentNineBinding
    lateinit var adapter: MyWaitingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNineBinding.inflate(inflater, container, false)

        val networkService = (context?.applicationContext as MyApplication).networkService

        val reserveListCall = networkService.getMyProduct("웰컴키트")

        var userId = ""
        val preferences = requireActivity().getSharedPreferences("login", AppCompatActivity.MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        reserveListCall.enqueue(object : Callback<ItemDataList> {
            override fun onResponse(call: Call<ItemDataList>, response: Response<ItemDataList>) {
                if (isAdded) {
                    var item = response.body()?.items
                    Log.d("lmj", "-------")
                    Log.d("lmj", "One item : $item")
                    Log.d("lmj", "===========")
                    Log.d("lmj", "실패 내용 : ${response.code()}")
                    adapter = MyWaitingsAdapter(requireContext(), item, username, networkService)

                    binding.nineRecyclerView.adapter = adapter
                    binding.nineRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
                    adapter.notifyDataSetChanged()
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