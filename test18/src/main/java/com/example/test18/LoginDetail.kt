package com.example.test18

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.example.test18.Model.UserList
import com.example.test18.databinding.ActivityLoginDetailBinding
import com.example.test18.recycler.MyUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.provider.Settings
import android.widget.Toast
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.InCart
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class LoginDetail : AppCompatActivity() {
    lateinit var binding: ActivityLoginDetailBinding
    lateinit var adapter: MyUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.Toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.Toolbar.title = "로그인 정보"
        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)
        Log.d("lmj", "-------$username------")

        val networkService = (applicationContext as MyApplication).networkService

        val userCall = networkService.getUser(username)

        userCall.enqueue(object : Callback<UserList> {
            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                var item = response.body()?.items
                Log.d("lmj", "-------")
                Log.d("lmj", "One item : $item")
                Log.d("lmj", "===========")
                Log.d("lmj", "실패 내용 : ${response.code()}")
                adapter = MyUserAdapter(item)
                binding.userRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })

        binding.updatebtn.setOnClickListener {

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // FCM 토큰
            val token = task.result
            Log.d("lmj", "FCM Token: $token")


            val networkService = (applicationContext as MyApplication).networkService
            val csrfCall = networkService.getCsrfToken()

                val myToken ="d-8C4XeUSKOhARxpdZmtgm:APA91bFkd7ILTsMMSy2TyFZ3mNLB7eLTOs5ntp2rBKO34-1191n29TXhPZhrfrKn8ya0GEY1fDGqE-vocNuni7JgfLINIV0N2QW9O-0-w3c0x6OnPXCh59SMMVq0Vtfw6r0y3e6-Qj7V"

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token
                    val notiTokenCall = networkService.notiToken(csrfToken, myToken)

                    notiTokenCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            Log.d("lmj", "토큰성공 : $myToken , 토큰 : $csrfToken")
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Log.d("lmj", "실패데이터 : ${t.message}")
                        }
                    })

                    val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
                    val deviceCall = networkService.notification(csrfToken, username, deviceId)

                    deviceCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            Log.d("lmj", "디바이스성공 : $deviceId , 토큰 : $csrfToken")
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

        }

        binding.logoutbtn.setOnClickListener {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.remove("username")
            editor.apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        binding.bottommenu.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.first_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }
                R.id.second_tab -> {
                    Log.d("lmj", "username==$username==")
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.third_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, LunaActivity::class.java)
                        startActivity(intent)
                    }
                }
                R.id.fourth_tab -> {
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, LoginDetail::class.java)
                        startActivity(intent)
                    }
                }
            }
            true
        }

    }
}