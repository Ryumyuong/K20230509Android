package lunamall.example.test18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import lunamall.example.test18.MyApplication
import lunamall.example.test18.databinding.ActivityCardBinding
import lunamall.example.test18.databinding.ActivityLoginAdminBinding
import lunamall.example.test18.model.CardList
import lunamall.example.test18.recycler.MyCardAdapter
import lunamall.example.test18.recycler.UserListAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CardActivity : AppCompatActivity() {
    lateinit var binding : ActivityCardBinding
    lateinit var adapter : MyCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "할부 내역"

        val networkService = (applicationContext as MyApplication).networkService

        val cardCall = networkService.cardList()

        cardCall.enqueue(object : Callback<CardList> {
            override fun onResponse(call: Call<CardList>, response: Response<CardList>) {
                var card = response.body()?.card
                adapter = MyCardAdapter(card)
                binding.cardRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CardList>, t: Throwable) {
                call.cancel()
            }

        })
    }
}