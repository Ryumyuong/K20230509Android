package lunamall.example.test18.recycler

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
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
import lunamall.example.test18.model.UserList
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyWaitingVIPViewHolder(val binding: ItemRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val button: Button = itemView.findViewById(R.id.orderbutton)
}

class MyWaitingVIPAdapter(val context:Context, datas: MutableList<Product>?, val username:String?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    var listDataFilter: MutableList<Product>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyWaitingVIPViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyWaitingViewHolder).binding
        val waiting = listDataFilter?.get(position)

        binding.itemtitle.text = waiting?.s_name
        binding.itemcontent.text = "더보기"
        binding.itemprice.text = waiting?.s_price.toString() + " 루나"

        holder.button.setOnClickListener {
            val userCall = networkService.getUser(username)

            userCall.enqueue(object : Callback<UserList> {
                override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                    var item = response.body()?.items

                    if (item?.get(0)?.vip.equals("VVIP")) {
                        val csrfCall = networkService.getCsrfToken()

                        csrfCall.enqueue(object : Callback<CsrfToken> {
                            override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                                val csrfToken = response.body()?.token
                                val cart = InCart(username, waiting?.s_name, waiting?.s_price, waiting?.s_description, waiting?.s_fileName)
                                val insertCall = networkService.insertCart(csrfToken, cart)

                                insertCall.enqueue(object : Callback<InCart> {
                                    override fun onResponse(call: Call<InCart>, response: Response<InCart>) {
                                        Toast.makeText(context, "장바구니에 추가되었습니다.", Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onFailure(call: Call<InCart>, t: Throwable) {
                                        call.cancel()
                                    }
                                })
                            }

                            override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                                call.cancel()
                            }
                        })

                    } else {
                        Toast.makeText(context, "VVIP만 구입 가능합니다.", Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onFailure(call: Call<UserList>, t: Throwable) {
                    call.cancel()
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

