package lunamall.example.test18

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import lunamall.example.test18.databinding.ActivityLoginAdminBinding
import lunamall.example.test18.model.UserList


class LoginAdmin : AppCompatActivity() {
    lateinit var binding: ActivityLoginAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
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
                binding.VIP.text = item?.get(0)?.vip
                binding.name.text = item?.get(0)?.username
                binding.luna.text = item?.get(0)?.money.toString() + " 루나"
                binding.luna.paintFlags = binding.luna.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                Log.d("lmj", "-------${item?.get(0)?.vip}------")
                Log.d("lmj", "-------${item?.get(0)?.username}------")
                Log.d("lmj", "-------${item?.get(0)?.money.toString()} 루나------")


            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                Log.d("lmj", "실패 내용 : ${t.message}")
                call.cancel()
            }

        })

        binding.itembtn.setOnClickListener {
            val intent = Intent(this, InsertProduct::class.java)
            startActivity(intent)
        }

        binding.addLuna.setOnClickListener {
            val intent = Intent(this,LunaAdd::class.java)
            startActivity(intent)
        }

        binding.insertbtn.setOnClickListener {
            val intent = Intent(this, InsertUser::class.java)
            startActivity(intent)
        }

        binding.updatebtn.setOnClickListener {

        }

        binding.logoutbtn.setOnClickListener {
            val editor: SharedPreferences.Editor = preferences.edit()
            editor.remove("username")
            editor.apply()

            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }


        binding.bottommenu.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.first_tab -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                }
                R.id.second_tab -> {
                        val intent = Intent(this, CartActivity::class.java)
                        startActivity(intent)
                }
                R.id.third_tab -> {
                        val intent = Intent(this, OrderActivity::class.java)
                        startActivity(intent)
                }
                R.id.fourth_tab -> {
                        val intent = Intent(this, LoginAdmin::class.java)
                        startActivity(intent)
                }
            }
            true
        }

    }
}