package lunamall.example.test18.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.databinding.CardRecyclerviewBinding
import lunamall.example.test18.model.Card

class MyCardViewHolder(val binding: CardRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyCardAdapter(datas: MutableList<Card>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var cardData: MutableList<Card>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyCardViewHolder(CardRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyCardViewHolder).binding
        val card = cardData?.get(position)

        binding.orderTime.text = card?.name.toString()
        binding.name.text = card?.order_time.toString()
        binding.days.text = card?.days.toString()
        binding.luna.text = card?.luna.toString() + " 루나"

    }

    override fun getItemCount(): Int {

        return cardData?.size ?: 0
    }

}

