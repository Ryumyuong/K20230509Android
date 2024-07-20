package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import lunamall.example.test18.databinding.ActivityAlarmBinding
import lunamall.example.test18.databinding.ActivityLoginAdminBinding
import lunamall.example.test18.model.AlarmList
import lunamall.example.test18.model.CardList
import lunamall.example.test18.model.CartList
import lunamall.example.test18.model.UserList
import lunamall.example.test18.recycler.MyAlarmAdapter
import lunamall.example.test18.recycler.MyCardAdapter
import lunamall.example.test18.recycler.MyCartAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlarmActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmBinding
    lateinit var adapter : MyAlarmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "알림 목록"

        val networkService = (applicationContext as MyApplication).networkService

        val alarmCall = networkService.getAlarm()

        alarmCall.enqueue(object : Callback<AlarmList> {
            override fun onResponse(call: Call<AlarmList>, response: Response<AlarmList>) {
                var alarm = response.body()?.alarm
                adapter = MyAlarmAdapter(alarm)
                binding.alarmRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<AlarmList>, t: Throwable) {
                call.cancel()
            }

        })
    }
}