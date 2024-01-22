package lunamall.example.test18.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.databinding.LunaRecyclerviewBinding
import lunamall.example.test18.model.Order


class MyLunaViewHolder(val binding: LunaRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyLunaAdapter(datas: MutableList<Order>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var lunaData: MutableList<Order>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyLunaViewHolder(LunaRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyLunaViewHolder).binding
        val luna = lunaData?.get(position)
        binding.orderTime.text = luna?.order_time.toString()
        binding.name.text = luna?.userId.toString()
        binding.orderMenu.text = luna?.order_menu.toString()
        binding.price.text = luna?.order_price.toString() + "루나"
    }

    override fun getItemCount(): Int {
        return lunaData?.size ?: 0
    }

}

