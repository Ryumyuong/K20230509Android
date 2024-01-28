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
import lunamall.example.test18.R
import lunamall.example.test18.databinding.ItemRecyclerviewBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.InCart
import lunamall.example.test18.model.Product
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyWaitingViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val button: Button = itemView.findViewById(R.id.orderbutton)
}

class MyWaitingAdapter(val context:Context, datas: MutableList<Product>?, val username:String?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var listDataFilter: MutableList<Product>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyWaitingViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyWaitingViewHolder).binding
        val waiting = listDataFilter?.get(position)
        Log.d("lmj", "waiting 값 : $waiting")

        binding.itemtitle.text = waiting?.s_name
        binding.itemcontent.text = "더보기"
        binding.itemprice.text = waiting?.s_price.toString() + " 루나"

        holder.button.setOnClickListener {
            if(username.equals("")) {
                Toast.makeText(context, "로그인을 먼저 하세요.", Toast.LENGTH_LONG).show()
            } else {
                val csrfCall = networkService.getCsrfToken()

                csrfCall.enqueue(object : Callback<CsrfToken> {
                    override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                        val csrfToken = response.body()?.token
                        Log.d("lmj", "토큰 : $csrfToken")
                        Log.d("lmj", "username : $username")
                        val cart = InCart(username, waiting?.s_name, waiting?.s_price, waiting?.s_description, waiting?.s_fileName)
                        Log.d("lmj", "데이터 : ${waiting?.s_price}")
                        Log.d("lmj", "토큰 : $csrfToken")
                        Log.d("lmj", "incart : $cart")
                        val insertCall = networkService.insertCart(csrfToken, cart)

                        insertCall.enqueue(object : Callback<InCart> {
                            override fun onResponse(call: Call<InCart>, response: Response<InCart>) {
                                Log.d("lmj", "성공 ${waiting?.s_name}, ${waiting?.s_price}, ${response.code()}")
                                Toast.makeText(context, "장바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<InCart>, t: Throwable) {
                                Log.d("lmj", "실패데이터 : ${t.message}")
                            }
                        })
                    }

                    override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                        Log.d("lmj", "실패토큰 : ${t.message}")
                    }
                })
            }


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
        // Open the link in a web browser
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

