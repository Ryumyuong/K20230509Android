package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import lunamall.example.test18.databinding.ActivityOrderMenuBinding
import lunamall.example.test18.model.Cart
import lunamall.example.test18.model.CartList
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.InCard
import lunamall.example.test18.model.InOrder
import lunamall.example.test18.model.UserList
import lunamall.example.test18.recycler.OrderMenuAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.ceil

class OrderMenu : AppCompatActivity() {
    lateinit var binding: ActivityOrderMenuBinding
    lateinit var adapter: OrderMenuAdapter
    private var selectedNumber = "일시불"
    lateinit var total: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        val networkService = (applicationContext as MyApplication).networkService

        val userCall = networkService.getUser(username)

        userCall.enqueue(object : Callback<UserList> {
            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                var item = response.body()?.items

                binding.name.text = item?.get(0)?.username
                binding.tel.setText(item?.get(0)?.phone)
                binding.add.setText(item?.get(0)?.address)
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                call.cancel()
            }

        })

        val cartCall = networkService.getCart(username)

        cartCall.enqueue(object : Callback<CartList> {
            override fun onResponse(call: Call<CartList>, response: Response<CartList>) {
                var item = response.body()?.items
                adapter = OrderMenuAdapter(item)
                binding.OrderMenuRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CartList>, t: Throwable) {
                call.cancel()
            }

        })

        val totalCall = networkService.total(username)

        totalCall.enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                var item = response.body()
                total = item?.total.toString()
                binding.total2.text = item?.total.toString()
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                call.cancel()
            }

        })

        val items = arrayOf("일시불", "2개월", "3개월", "4개월", "5개월", "6개월", "7개월", "8개월", "9개월", "10개월", "11개월", "12개월")

        val defaultSelection = 0
        binding.plan.text = items[defaultSelection]

        binding.plan.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            builder.setTitle("할부 개월수")
                .setSingleChoiceItems(items, defaultSelection) { dialog, which ->
                    val selectedValue = items[which]
                    if(selectedValue != "일시불") {
                        selectedNumber = selectedValue.substringBefore("개월").trim()


                        val card = total.toDoubleOrNull()?.div(selectedNumber.toInt())
                        if (card != null) {
                            val roundup = ceil(card).toInt()
                            binding.total2.text = roundup.toString()
                        }
                    }else {
                        binding.total2.text = total

                    }
                    adapter.notifyDataSetChanged()
                    binding.plan.text = selectedValue
                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()
        }

        binding.orderCom.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token

                    val userCall = networkService.getUser(username)

                    userCall.enqueue(object : Callback<UserList> {
                        override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                            var item = response.body()?.items
                            if (total != null) {
                                if(item?.get(0)?.money?:0 >= binding.total2.text.toString().toInt()) {
                                    Log.d("lmj", "select : $selectedNumber")
                                    val order = InOrder(username, binding.tel.text.toString(), binding.add.text.toString(), binding.inquire.text.toString(), selectedNumber, total.toInt())
                                    val orderCall = networkService.order(csrfToken, order, binding.plan.text.toString(), binding.total2.text.toString().toInt())
                                    orderCall.enqueue(object : Callback<InOrder> {
                                        override fun onResponse(call: Call<InOrder>, response: Response<InOrder>) {
                                            val notiTokenCall = networkService.notiToken(csrfToken, username)

                                            notiTokenCall.enqueue(object : Callback<Unit> {
                                                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                                                }

                                                override fun onFailure(call: Call<Unit>, t: Throwable) {
                                                    call.cancel()
                                                }
                                            })
                                            Toast.makeText(this@OrderMenu, "주문이 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this@OrderMenu, OrderActivity::class.java)
                                            startActivity(intent)
                                        }

                                        override fun onFailure(call: Call<InOrder>, t: Throwable) {
                                            Toast.makeText(this@OrderMenu, "주문이 실패하었습니다.", Toast.LENGTH_SHORT).show()
                                        }
                                    })

                                }
                                else {
                                    Toast.makeText(this@OrderMenu, "루나가 부족합니다.", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }

                        override fun onFailure(call: Call<UserList>, t: Throwable) {
                            call.cancel()
                        }

                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    call.cancel()
                }
            })

        }
    }
}