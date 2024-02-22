package lunamall.example.test18

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import lunamall.example.test18.databinding.ActivityLoginDetailBinding
import lunamall.example.test18.model.UserList
import lunamall.example.test18.recycler.MyUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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

        val bottomNavigationView = binding.bottommenu

        bottomNavigationView.selectedItemId = R.id.fourth_tab

        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        val networkService = (applicationContext as MyApplication).networkService

        val userCall = networkService.getUser(username)

        userCall.enqueue(object : Callback<UserList> {
            override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                var item = response.body()?.items
                adapter = MyUserAdapter(this@LoginDetail, item)
                binding.userRecyclerView.adapter = adapter
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<UserList>, t: Throwable) {
                call.cancel()
            }

        })

        binding.addluna.setOnClickListener {
            val intent = Intent(this, LunaUserActivity::class.java)
            startActivity(intent)
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
                    if(username.equals("")) {
                        val intent = Intent(this, Login::class.java)
                        startActivity(intent)
                    }else {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }

                }
                R.id.second_tab -> {
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