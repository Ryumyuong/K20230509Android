package lunamall.example.test18.recycler

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import lunamall.example.test18.R
import lunamall.example.test18.databinding.CartRecyclerviewBinding
import lunamall.example.test18.model.Cart
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.InCart
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class MyCartViewHolder(val binding: CartRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {
    val plus: ImageView = itemView.findViewById(R.id.plus)
    val minus: ImageView = itemView.findViewById(R.id.minus)
    val cancle: ImageView = itemView.findViewById(R.id.cancle)
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
        var total = 0
        var i = 0


        binding.itemname.text = cart?.s_name.toString()
        binding.count.text = cart?.count.toString()
        binding.total.text = cart?.cost.toString() + " 루나"

        val urlImg = cart?.fileName

        if (urlImg != null) {
            val bitmap = decodeBase64(urlImg)
            binding.imageView.setImageBitmap(bitmap)
        } else {

        }

        if(binding.count.text == "1") {
            holder.minus.isClickable = false
        }

        holder.plus.setOnClickListener {
            count = count?.plus(1)
            if (price != null) {
                total = price * count!!
            }
            holder.minus.isClickable = true
            cart?.count = count
            cart?.total = total

            notifyDataSetChanged()

            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token
                    val cart = InCart(username, cart?.s_name, cart?.s_price, cart?.s_description, cart?.fileName)

                    val insertCall = networkService.insertCart(csrfToken, cart)

                    insertCall.enqueue(object : Callback<InCart> {
                        override fun onResponse(call: Call<InCart>, response: Response<InCart>) {

                        }

                        override fun onFailure(call: Call<InCart>, t: Throwable) {
                            Log.d("lmj", "실패 내용 : ${t.message}")
                            call.cancel()
                        }

                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {

                }
            })
        }

        holder.minus.setOnClickListener {
            if(count == 1) {
                holder.minus.isClickable = false
            } else {
                count = count?.minus(1)
                if (price != null) {
                    total = price * count!!
                }
                cart?.count = count
                cart?.total = total
                notifyDataSetChanged()

                val id = cart?.c_id
                val csrfCall = networkService.getCsrfToken()

                csrfCall.enqueue(object : Callback<CsrfToken> {
                    override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                        val csrfToken = response.body()?.token

                        val deleteCall = networkService.delete(csrfToken, id)

                        deleteCall.enqueue(object : Callback<Unit> {
                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

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

        holder.cancle.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token

                    val deleteCall = networkService.deleteCart(csrfToken, username, itemName)

                    deleteCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            cartData?.removeAt(position)

                            notifyItemRemoved(position)
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

