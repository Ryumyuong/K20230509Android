package lunamall.example.test18.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.databinding.AlarmRecyclerviewBinding
import lunamall.example.test18.model.Alarm

class MyAlarmViewHolder(val binding: AlarmRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyAlarmAdapter(datas: MutableList<Alarm>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var alarmData: MutableList<Alarm>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyAlarmViewHolder(AlarmRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyAlarmViewHolder).binding
        val alarm = alarmData?.get(position)

        binding.orderTime.text = alarm?.a_time.toString()
        binding.name.text = alarm?.a_username.toString()
        binding.menu.text = alarm?.a_menu.toString()
        binding.luna.text = alarm?.a_price.toString() + " 루나"

    }

    override fun getItemCount(): Int {

        return alarmData?.size ?: 0
    }

}

