package lunamall.example.test18.recycler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.R
import lunamall.example.test18.databinding.CartRecyclerviewBinding
import lunamall.example.test18.databinding.OrderMenuRecyclerviewBinding
import lunamall.example.test18.model.Cart
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderMenuViewHolder(val binding: OrderMenuRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class OrderMenuAdapter(datas: MutableList<Cart>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var cartData: MutableList<Cart>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        OrderMenuViewHolder(OrderMenuRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as OrderMenuViewHolder).binding
        val cart = cartData?.get(position)

        binding.itemname.text = cart?.s_name.toString()
        binding.count.text = "수량 " + cart?.count.toString() + "개 / "
        binding.total.text = cart?.cost.toString() + "루나"

        val urlImg = cart?.fileName

        if (urlImg != null) {
            val bitmap = decodeBase64(urlImg)
            binding.imageView.setImageBitmap(bitmap)
        } else {

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

