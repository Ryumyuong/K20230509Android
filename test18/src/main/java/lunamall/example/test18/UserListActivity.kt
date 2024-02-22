package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import lunamall.example.test18.databinding.ActivityUserListBinding
import lunamall.example.test18.recycler.UserListAdapter
import lunamall.example.test18.model.UserList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListActivity : AppCompatActivity() {
    lateinit var binding: ActivityUserListBinding
    lateinit var adapter: UserListAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityUserListBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            binding.toolbar.title = "회원 목록"

            var userId = ""
            val preferences = getSharedPreferences("login", MODE_PRIVATE)
            val username = preferences.getString("username", userId)

            val networkService = (applicationContext as MyApplication).networkService

            var pageNumber = 1
            var pageSize = 10
            binding.before.isInvisible = true

            val userCall = networkService.userList(pageNumber, pageSize)

            userCall.enqueue(object : Callback<UserList> {
                override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                    var item = response.body()?.items
                    adapter = UserListAdapter(this@UserListActivity, item, networkService)
                    binding.userRecyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<UserList>, t: Throwable) {
                    call.cancel()
                }

            })

            binding.before.setOnClickListener {
                pageNumber = pageNumber.minus(1)
                if(pageNumber == 1) {
                    binding.before.isInvisible = true
                }

                val userCall = networkService.userList(pageNumber, pageSize)

                userCall.enqueue(object : Callback<UserList> {
                    override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                        var item = response.body()?.items
                        adapter = UserListAdapter(this@UserListActivity, item, networkService)
                        binding.userRecyclerView.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<UserList>, t: Throwable) {
                        call.cancel()
                    }

                })
            }

            binding.next.setOnClickListener {
                pageNumber = pageNumber.plus(1)

                val userCall = networkService.userList(pageNumber, pageSize)

                userCall.enqueue(object : Callback<UserList> {
                    override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                        var item = response.body()?.items
                        adapter = UserListAdapter(this@UserListActivity, item, networkService)
                        binding.userRecyclerView.adapter = adapter
                        if(item?.count()!! < 10) {
                            binding.next.isInvisible = true
                            binding.before.isInvisible = false
                        }
                        adapter.notifyDataSetChanged()
                    }

                    override fun onFailure(call: Call<UserList>, t: Throwable) {
                        call.cancel()
                    }

                })
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
                        }else if(username=="admin" || username =="류지희" || username == "고혜영" || username == "정진경") {
                            val intent = Intent(this, InsertProduct::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this, CartActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    R.id.third_tab -> {
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
                    R.id.fourth_tab -> {
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
                true
            }
    }
}