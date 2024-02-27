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

        supportActionBar?.setDisplayShowTitleEnabled(false)


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

        binding.main.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.home.setOnClickListener{
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.cart.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경") {
                val intent = Intent(this, InsertProduct::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, CartActivity::class.java)
                startActivity(intent)
            }
        }

        binding.list.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경"){
                val intent = Intent(this, UserListActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LunaActivity::class.java)
                startActivity(intent)
            }
        }

        binding.profile.setOnClickListener {
            if(username.equals("")) {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경"){
                val intent = Intent(this, LoginAdmin::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginDetail::class.java)
                startActivity(intent)
            }
        }

    }
}