package com.example.test11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test11.databinding.ActivityMain4Binding
import com.example.test11.databinding.Item342Binding

class MainActivity4 : AppCompatActivity() {

    class MyViewHolder (val binding: Item342Binding) : RecyclerView.ViewHolder(binding.root)

    class MyAdapter(val datas: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            MyViewHolder(Item342Binding.inflate(LayoutInflater.from(parent.context), parent, false))

        override fun getItemCount(): Int {
            Log.d("lmj", "init datas size 크기 : ${datas.size}")
            return datas.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val binding = (holder as MyViewHolder).binding
            // 데이터를 뷰랑 연결
            binding.itemData.text = datas[position]
            //뷰에 이벤트 추가
            binding.itemRoot.setOnClickListener {
                /*Toast.makeText(this@MyAdapter, "메세지 요소 인덱스 : $position", Toast.LENGTH_SHORT).show()*/
                Log.d("lmj", "클릭시 이벤트 발생, 해당 아이템의 요소 : $position")
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val datas = mutableListOf<String>()
        for(i in 1..9) {
            datas.add("Item $i")
        }
        //설정 적용
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = MyAdapter(datas)
        // 옵션
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )

    }
}

