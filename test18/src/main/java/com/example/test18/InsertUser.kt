package com.example.test18

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.OrderList
import com.example.test18.Model.Product
import com.example.test18.Model.User
import com.example.test18.Model.UserList
import com.example.test18.databinding.ActivityInsertProductBinding
import com.example.test18.databinding.ActivityInsertUserBinding
import com.example.test18.databinding.ActivityUpdateBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertUser : AppCompatActivity() {
    lateinit var binding: ActivityInsertUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "회원 등록"
        val id = binding.id.text.toString()
        val password = binding.password.text.toString()
        val name = binding.name.text.toString()
        val phone = binding.phone.text.toString()
        val address = binding.address.text.toString()
        val money = 0
        val kit = "X"
        val vip = binding.vip.text.toString()

        val networkService = (applicationContext as MyApplication).networkService

        val user = User(id,password, name, phone, address, money, vip, kit)
        val csrfCall = networkService.getCsrfToken()

        csrfCall.enqueue(object : Callback<CsrfToken> {
            override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                val csrfToken = response.body()?.token
                val userCall = networkService.newLogin(csrfToken, user)

                userCall.enqueue(object : Callback<User> {
                    override fun onResponse(call: Call<User>, response: Response<User>) {

                    }

                    override fun onFailure(call: Call<User>, t: Throwable) {
                        Log.d("lmj", "실패 내용 : ${t.message}")
                        call.cancel()
                    }

                })
            }

            override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }
        })



        binding.vip.setOnClickListener{
            val items = arrayOf("일반 회원", "VIP", "VVIP")

            // AlertDialog 빌더 생성
            val builder = AlertDialog.Builder(this)
            builder.setTitle("등급 선택")
                .setItems(items) { dialog, which ->
                    // 사용자가 목록에서 항목을 선택했을 때 실행되는 코드
                    val selectedValue = items[which]
                    binding.vip.text = selectedValue
                    // 선택한 항목에 대한 작업 수행
                    // 예: 토스트 메시지 표시 등
                }

            // AlertDialog 생성 및 표시
            val dialog = builder.create()
            dialog.show()
        }

    }
}