package com.example.test18

import android.content.Intent
import android.os.Build.VERSION_CODES.P
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test18.Model.CsrfToken
import com.example.test18.Model.UserList
import com.example.test18.databinding.ActivityLoginBinding
import com.example.test18.recycler.MyUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Login : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var adapter: MyUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        var buttonLogin = binding.buttonLogin

        // 로그인 버튼 클릭 리스너 설정
        buttonLogin.setOnClickListener {
            var editTextUsername = binding.editTextUsername.text.toString()
            var editTextPassword = binding.editTextPassword.text.toString()
            // 로그인 버튼 클릭 시 수행할 동작
            val username: String = editTextUsername
            val password: String = editTextPassword

            Log.d("lmj", "username = $username, password : $password")

            val networkService = (applicationContext as MyApplication).networkService
            val userCall = networkService.getUser(username)

            userCall.enqueue(object : Callback<UserList> {
                override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                    val item = response.body()?.items
                    if (item!!.isNotEmpty()) {
                            if(username == "admin") {
                                if(password == "1234") {
                                    val preferences = getSharedPreferences("login", MODE_PRIVATE)
                                    val editor = preferences.edit()
                                    editor.putString("username", username)
                                    editor.apply()
                                    Toast.makeText(this@Login, "로그인이 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@Login, LoginAdmin::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this@Login, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                if(password == "0000") {
                                    val preferences = getSharedPreferences("login", MODE_PRIVATE)
                                    val editor = preferences.edit()
                                    editor.putString("username", username)
                                    editor.apply()
                                    Toast.makeText(this@Login, "로그인이 성공하였습니다.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@Login, LoginDetail::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(this@Login, "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show()
                                }
                            }

                    } else {
                            Toast.makeText(this@Login, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onFailure(call: Call<UserList>, t: Throwable) {
                }
            })
        }


    }
}