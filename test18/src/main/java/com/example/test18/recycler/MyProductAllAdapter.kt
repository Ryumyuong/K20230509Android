package com.example.test18.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import com.example.test18.retrofit.INetworkService
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.test18.InsertProduct
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.ItemDataList
import com.example.test18.Model.Product
import com.example.test18.R
import com.example.test18.UpdateActivity
import com.example.test18.databinding.ItemAllRecyclerviewBinding
import okio.ByteString.decodeBase64
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyProductAllViewHolder(val binding: ItemAllRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val button1: Button = itemView.findViewById(R.id.updatebutton)
    val button2: Button = itemView.findViewById(R.id.deletebutton)
}

class MyProductAllAdapter(val context:Context, datas: MutableList<Product>?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var listDataFilter: MutableList<Product>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyProductAllViewHolder(ItemAllRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyProductAllViewHolder).binding
        val waiting = listDataFilter?.get(position)
        Log.d("lmj", "waiting 값 : $waiting")

        binding.itemtitle.text = waiting?.s_name
        binding.itemprice.text = waiting?.s_price.toString() + " 루나"

        holder.button1.setOnClickListener {
            val intent = Intent(context, InsertProduct::class.java)
            intent.putExtra("product", waiting?.s_name)
            context.startActivity(intent)
        }

        holder.button2.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token

                    val deleteCall = networkService.deleteProduct(csrfToken, waiting?.s_name)

                    deleteCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            Log.d("lmj", "성공 내용 : ${response.code()}")
                            Toast.makeText(context, "물품이 삭제되었습니다.", Toast.LENGTH_LONG).show()
                            notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Log.d("lmj", "실패 내용 : ${t.message}")
                            call.cancel()
                        }

                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    Log.d("lmj", "실패토큰 : ${t.message}")
                }
            })

        }

        val urlImg = waiting?.s_fileName

        if (urlImg != null) {
            val bitmap = decodeBase64(urlImg)
            binding.imageView.setImageBitmap(bitmap)
        } else {

        }

    }

    override fun getItemCount(): Int {
        return listDataFilter?.size ?: 0
    }

    private fun decodeBase64(base64Image: String?): Bitmap? {
        if (!base64Image.isNullOrEmpty()) {
            val decodedString: ByteArray = Base64.decode(base64Image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }
        return null
    }

}

