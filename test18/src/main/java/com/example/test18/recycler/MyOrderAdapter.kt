package com.example.test18.recycler

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.test18.Model.Order
import com.example.test18.databinding.OrderRecyclerviewBinding


class MyOrderViewHolder(val binding: OrderRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyOrderAdapter(datas: MutableList<Order>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var orderData: MutableList<Order>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyOrderViewHolder(OrderRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyOrderViewHolder).binding
        val order = orderData?.get(position)

        binding.orderTime.text = order?.order_time.toString()
        binding.username.text = order?.userId.toString()
        binding.phone.text = order?.phone.toString()
        binding.orderMenu.text = order?.order_menu.toString()
        binding.price.text = order?.order_price.toString() + " 루나"


    }

    override fun getItemCount(): Int {
        return orderData?.size ?: 0
    }

}

