package lunamall.example.test18.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.InsertProduct
import lunamall.example.test18.R
import lunamall.example.test18.UpdateProduct
import lunamall.example.test18.databinding.UpdateProductRecyclerviewBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.Product
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateProductViewHolder(val binding: UpdateProductRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val button: Button = itemView.findViewById(R.id.updatebutton)
    val button2: Button = itemView.findViewById(R.id.deletebutton)
}

class UpdateProductAdapter(val context:Context, datas: MutableList<Product>?, val username:String?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var listDataFilter: MutableList<Product>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        UpdateProductViewHolder(UpdateProductRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as UpdateProductViewHolder).binding
        val waiting = listDataFilter?.get(position)
        holder.button2.setBackgroundColor(R.drawable.actions)

        binding.itemtitle.text = waiting?.s_name
        binding.itemcontent.text = "더보기"
        binding.itemprice.text = waiting?.s_price.toString() + " 루나"

        holder.button.setOnClickListener {
            val intent = Intent(context, UpdateProduct::class.java)
            intent.putExtra("productName",waiting?.s_name)
            context.startActivity(intent)
        }

        holder.button2.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token

                    val deleteCall = networkService.deleteProduct(csrfToken,waiting?.s_name)

                   deleteCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            Toast.makeText(context,"물품이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                            notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
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

        binding.itemcontent.setOnClickListener {
            val link = waiting?.s_description
            if (!link.isNullOrBlank()) {
                openLink(link)
            }
        }
    }

    private fun openLink(link: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
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

