package com.example.test18.recycler

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.test18.Model.User
import com.example.test18.databinding.UserRecyclerviewBinding


class MyUserViewHolder(val binding: UserRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyUserAdapter(datas: MutableList<User>?): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var userData: MutableList<User>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyUserViewHolder(UserRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyUserViewHolder).binding
        val user = userData?.get(position)

        binding.userId.text = "이름 : " + user?.userId
        binding.money.text = user?.money.toString() + " 루나"
        binding.phone.text = user?.phone
        binding.address.text = user?.address

    }

    override fun getItemCount(): Int {
        return userData?.size ?: 0
    }

}

