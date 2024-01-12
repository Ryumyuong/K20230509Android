package com.example.test18

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

            // 간단한 로그인 체크 (실제로는 서버와 통신하여 확인)
            if (username == "admin" && password == "1234") {
                Log.d("lmj", "로그인 성공")
                val preferences = getSharedPreferences("login", MODE_PRIVATE)
                val editor = preferences.edit()
                editor.putString("username", username)
                editor.apply()
                Log.d("lmj", "로그인 저장")
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginDetail::class.java)
                startActivity(intent)
                Log.d("lmj", "이동")

            } else {
                Log.d("lmj", "로그인 실패")
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }


    }
}