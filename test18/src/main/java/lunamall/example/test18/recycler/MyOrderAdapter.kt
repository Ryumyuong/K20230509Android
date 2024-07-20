package lunamall.example.test18.recycler

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import lunamall.example.test18.databinding.OrderRecyclerviewBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.Order
import lunamall.example.test18.retrofit.INetworkService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyOrderViewHolder(val binding: OrderRecyclerviewBinding): RecyclerView.ViewHolder(binding.root) {

}

class MyOrderAdapter(val context: Context, datas: MutableList<Order>?, val networkService: INetworkService): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var orderData: MutableList<Order>? = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        MyOrderViewHolder(OrderRecyclerviewBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyOrderViewHolder).binding
        val order = orderData?.get(position)
        val id = order?.id.toString().toIntOrNull()
        val ids = order?.id.toString()
        val username = order?.userId.toString()

        binding.orderTime.text = order?.order_time.toString()
        binding.username.text = order?.userId.toString()
        binding.phone.text = order?.phone.toString()
        binding.address.text = order?.address.toString()
        binding.orderMenu.text = order?.order_menu.toString()
        binding.inquire.text = order?.inquire.toString()
        binding.price.text = order?.order_price.toString() + " 루나"

        var isClickable1 = true
        var isClickable2 = true

        if(order?.deliver.toString() == "배송중") {

        } else if(order?.deliver.toString() == "배송완료") {

        }

        binding.deliver.setOnClickListener {
            if(isClickable1) {
                val csrfCall = networkService.getCsrfToken()

                csrfCall.enqueue(object : Callback<CsrfToken> {
                    override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                        val csrfToken = response.body()?.token
                        val deliver = "O"
                        val updateCall = networkService.deliver(csrfToken, username, deliver)

                        updateCall.enqueue(object : Callback<Unit> {
                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                Toast.makeText(context, "배송 시작 알람이 전송되었습니다.",Toast.LENGTH_SHORT).show()
                            }

                            override fun onFailure(call: Call<Unit>, t: Throwable) {

                            }
                        })

                        val orderCall = networkService.delivering(csrfToken, id, username)

                        orderCall.enqueue(object : Callback<Unit> {
                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                            }

                            override fun onFailure(call: Call<Unit>, t: Throwable) {

                            }
                        })
                    }

                    override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    }
                })
                isClickable1 = false
            } else {
                Toast.makeText(context,"배송 중입니다.",Toast.LENGTH_SHORT).show()
            }
        }

        binding.complete.setOnClickListener {
            if(isClickable2) {
                val csrfCall = networkService.getCsrfToken()

                csrfCall.enqueue(object : Callback<CsrfToken> {
                    override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                        val csrfToken = response.body()?.token
                        val orderCall = networkService.deliverCom(csrfToken, ids, username)

                        orderCall.enqueue(object : Callback<Unit> {
                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                            }

                            override fun onFailure(call: Call<Unit>, t: Throwable) {

                            }
                        })
                    }

                    override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    }
                })
                isClickable2 = false
            } else {
                Toast.makeText(context,"배송 완료되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }

        binding.back.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token
                    val orderCall = networkService.deliverCom(csrfToken, ids, username)

                    orderCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {

                        }
                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                }
            })
        }
    }

    override fun getItemCount(): Int {
        return orderData?.size ?: 0
    }

    fun addItems(newItems: List<Order>) {
        orderData?.addAll(newItems)
        notifyDataSetChanged()
    }

}

