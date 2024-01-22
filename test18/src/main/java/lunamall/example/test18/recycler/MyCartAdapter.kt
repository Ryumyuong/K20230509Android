package lunamall.example.test18.recycler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.databinding.CartRecyclerviewBinding
import lunamall.example.test18.model.Cart


class MyCartViewHolder(val binding: CartRecyclerviewBinding): RecyclerView.ViewHolder(binding.root)

class MyCartAdapter(datas: MutableList<Cart>?): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var cartData: MutableList<Cart>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyCartViewHolder(CartRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyCartViewHolder).binding
        val cart = cartData?.get(position)

        binding.itemname.text = cart?.s_name.toString()
        binding.count.text = cart?.count.toString() + " 개"
        binding.price.text = cart?.s_price.toString() + " 루나"
        binding.total.text = "총 " + cart?.cost.toString() + " 루나"

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

