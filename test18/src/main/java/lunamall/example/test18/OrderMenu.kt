package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import lunamall.example.test18.databinding.ActivityOrderMenuBinding
import lunamall.example.test18.model.Cart
import lunamall.example.test18.model.CartList
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.InOrder
import lunamall.example.test18.model.UserList
import lunamall.example.test18.recycler.MyCartAdapter
import lunamall.example.test18.recycler.OrderMenuAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderMenu : AppCompatActivity() {
    lateinit var binding: ActivityOrderMenuBinding
    lateinit var adapter: OrderMenuAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "주문 내역"

        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)
        Log.d("lmj", "===username===$username===")

        val networkService = (applicationContext as MyApplication).networkService

        val userCall = networkService.getUser(username)

        userCall.enqueue(object : Callback<UserList> {
            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One User : $item")
                Log.d("lmj", "===========")

                binding.name.text = item?.get(0)?.username
                binding.tel.setText(item?.get(0)?.phone)
                binding.add.setText(item?.get(0)?.address)
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })

        val cartCall = networkService.getCart(username)

        cartCall.enqueue(object : Callback<CartList> {
            override fun onResponse(call: Call<CartList>, response: Response<CartList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One Cart : $item")
                Log.d("lmj", "===========")
                adapter = OrderMenuAdapter(item)
                binding.OrderMenuRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<CartList>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })

        val totalCall = networkService.total(username)

        totalCall.enqueue(object : Callback<Cart> {
            override fun onResponse(call: Call<Cart>, response: Response<Cart>) {
                var item = response.body()
                Log.d("lmj", "-------")
                Log.d("lmj", "One total : ${item?.total.toString()}")
                Log.d("lmj", "===========")
                binding.total2.text = item?.total.toString()
                val editor = preferences.edit()
                editor.putString("total", item?.total.toString())
                editor.apply()
            }

            override fun onFailure(call: Call<Cart>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })

        binding.orderCom.setOnClickListener {
            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token
                    Log.d("lmj", "토큰 : $csrfToken")
                    Log.d("lmj", "username : $username")

                    val userCall = networkService.getUser(username)

                    userCall.enqueue(object : Callback<UserList> {
                        override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                            var item = response.body()?.items
                            val total = ""
                            val totals = preferences.getString("total", total)
                            Log.d("lmj", "total : $totals")
                            if (totals != null) {
                                Log.d("lmj", "total ${item?.get(0)?.money?:0 >= totals.toInt()}")
                                if(item?.get(0)?.money?:0 >= totals.toInt()) {
                                    Log.d("lmj", "숫자로 변환")
                                    val order = InOrder(username, binding.tel.text.toString(), binding.add.text.toString(), binding.inquire.text.toString(), totals.toInt())
                                    val orderCall = networkService.order(csrfToken, order)
                                    orderCall.enqueue(object : Callback<InOrder> {
                                        override fun onResponse(call: Call<InOrder>, response: Response<InOrder>) {
                                            Log.d("lmj", "서버 응답 코드: ${response.code()}")
                                            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                                                if (!task.isSuccessful) {
                                                    return@OnCompleteListener
                                                    Log.d("lmj", "tast 성공 : ${task.isSuccessful}")
                                                }


                                                val token = task.result
                                                val myToken = ""
                                                val preferences = getSharedPreferences("code", MODE_PRIVATE)
                                                val code = preferences.getString("code", myToken)
                                                val networkService = (applicationContext as MyApplication).networkService
                                                val csrfCall = networkService.getCsrfToken()

                                                csrfCall.enqueue(object : Callback<CsrfToken> {
                                                    override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                                                        val csrfToken = response.body()?.token

                                                        val notiTokenCall = networkService.notiToken(csrfToken, token, code)

                                                        notiTokenCall.enqueue(object : Callback<Unit> {
                                                            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

                                                            }

                                                            override fun onFailure(call: Call<Unit>, t: Throwable) {
                                                                Log.d("lmj", "실패데이터 : ${t.message}")
                                                            }
                                                        })
                                                    }

                                                    override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                                                        Log.d("lmj", "실패토큰 : ${t.message}")
                                                    }
                                                })
                                            })
                                            Toast.makeText(this@OrderMenu, "주문이 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                            val intent = Intent(this@OrderMenu, OrderActivity::class.java)
                                            startActivity(intent)
                                        }

                                        override fun onFailure(call: Call<InOrder>, t: Throwable) {
                                            Log.d("lmj", "${t.message}")
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
                            Log.d("lmj", "실패 내용 : ${t.message}")
                            call.cancel()
                        }

                    })


                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    Log.d("lmj", "실패토큰 : ${t.message}")
                    call.cancel()
                }
            })

        }
    }
}