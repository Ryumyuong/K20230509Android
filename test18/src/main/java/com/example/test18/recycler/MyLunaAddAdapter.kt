package com.example.test18.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.test18.LunaActivity
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.User
import com.example.test18.Model.UserList
import com.example.test18.R
import com.example.test18.databinding.LunaAddRecyclerviewBinding
import com.example.test18.databinding.UserRecyclerviewBinding
import com.example.test18.retrofit.INetworkService
import com.google.android.material.textview.MaterialTextView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyLunaAddViewHolder(val binding: LunaAddRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val button1: MaterialTextView = itemView.findViewById(R.id.add)
    val button2: MaterialTextView = itemView.findViewById(R.id.min)
}

class MyLunaAddAdapter(val context: Context, datas: MutableList<User>?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var userData: MutableList<User>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyLunaAddViewHolder(LunaAddRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyLunaAddViewHolder).binding
        val user = userData?.get(position)
        binding.name.text = "이름 : " + user?.userId
        binding.phone.text = user?.phone
        binding.price.text = user?.money.toString() + " 루나"

        holder.button1.setOnClickListener {
            val money = binding.addLuna.text
            user?.money = money.toString().toIntOrNull()

            if(money != null) {
                val csrfCall = networkService.getCsrfToken()

                csrfCall.enqueue(object : Callback<CsrfToken> {
                    override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                        val csrfToken = response.body()?.token

                        val addLunaCall = networkService.addLuna(csrfToken, user?.userId, user)

                        addLunaCall.enqueue(object : Callback<Unit> {
                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                Toast.makeText(context, "루나가 추가되었습니다.", Toast.LENGTH_LONG).show()
                                notifyDataSetChanged()
                            }

                            override fun onFailure(call: Call<Unit>, t: Throwable) {
                                Log.d("lmj", "실패토큰 : ${t.message}")
                            }
                        })
                    }
                    override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                        Log.d("lmj", "실패토큰 : ${t.message}")
                    }
                })
            } else {
                Toast.makeText(context, "루나를 입력하세요", Toast.LENGTH_LONG).show()
            }

        }

        holder.button2.setOnClickListener {
            val mon = user?.money.toString()
            val money = binding.minLuna.text
            val myMoney = mon.toIntOrNull()
            val moneyValue = money.toString().toInt()

            if (mon != null) {
                if((myMoney?.minus(moneyValue))!! <0) {
                    Toast.makeText(context, "루나가 부족합니다.", Toast.LENGTH_LONG).show()
                } else {
                    if(moneyValue!=null) {
                        val csrfCall = networkService.getCsrfToken()

                        csrfCall.enqueue(object : Callback<CsrfToken> {
                            override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                                val csrfToken = response.body()?.token

                                val minLunaCall = networkService.minLuna(csrfToken, user?.userId, user)

                                minLunaCall.enqueue(object : Callback<Unit> {
                                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                        Toast.makeText(context, "루나가 제거되었습니다.", Toast.LENGTH_LONG).show()
                                        notifyDataSetChanged()
                                    }

                                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                                        Log.d("lmj", "실패토큰 : ${t.message}")
                                    }
                                })
                            }
                            override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                                Log.d("lmj", "실패토큰 : ${t.message}")
                            }
                        })
                    } else {
                        Toast.makeText(context, "루나를 입력하세요", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(context, "mon이 null", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun getItemCount(): Int {
        return userData?.size ?: 0
    }

}

