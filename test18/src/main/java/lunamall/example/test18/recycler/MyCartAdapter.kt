package lunamall.example.test18.recycler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.R
import lunamall.example.test18.databinding.CartRecyclerviewBinding
import lunamall.example.test18.model.Cart
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.InCart
import lunamall.example.test18.retrofit.INetworkService
import okio.ByteString.decodeBase64
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyCartViewHolder(val binding: CartRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val plus: TextView = itemView.findViewById(R.id.plus)
    val minus: TextView = itemView.findViewById(R.id.minus)
    val cancle: TextView = itemView.findViewById(R.id.cancle)
}

class MyCartAdapter(val username:String?, datas: MutableList<Cart>?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var cartData: MutableList<Cart>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyCartViewHolder(CartRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyCartViewHolder).binding
        val cart = cartData?.get(position)

        val itemName = cart?.s_name.toString()
        var count = cart?.count
        val price = cart?.s_price
        var total = cart?.cost

        binding.itemname.text = cart?.s_name.toString()
        binding.count.text = cart?.count.toString()
        binding.total.text = cart?.cost.toString() + " 루나"

        val urlImg = cart?.fileName

        if (urlImg != null) {
            val bitmap = decodeBase64(urlImg)
            binding.imageView.setImageBitmap(bitmap)
        } else {

        }

        var isClickable = true

        if(binding.count.text == "1") {
            isClickable = false
        }

        holder.plus.setOnClickListener {
            count = count?.plus(1)
            total = price?.let { it1 -> total?.plus(it1) }
            isClickable = true
            notifyDataSetChanged()
        }

        holder.minus.setOnClickListener {
            if(isClickable) {
                count = count?.minus(1)
                total = price?.let { it1 -> total?.minus(it1) }
                if(count == 1) {
                    isClickable = false
                }
                notifyDataSetChanged()
            }

        }

        holder.cancle.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token

                    val deleteCall = networkService.deleteCart(csrfToken, username, itemName)

                    deleteCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            notifyDataSetChanged()
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Log.d("lmj", "실패 내용 : ${t.message}")
                            call.cancel()
                        }

                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {

                }
            })
        }

    }

    override fun getItemCount(): Int {

        return cartData?.size ?: 0
    }

    private fun decodeBase64(base64Image: String?): Bitmap? {
        if (!base64Image.isNullOrEmpty()) {
            val decodedString: ByteArray = Base64.decode(base64Image, Base64.DEFAULT)
            return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        }
        return null
    }

}

