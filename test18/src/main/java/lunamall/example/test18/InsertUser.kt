package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import lunamall.example.test18.databinding.ActivityInsertUserBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertUser : AppCompatActivity() {
    lateinit var binding: ActivityInsertUserBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityInsertUserBinding.inflate(layoutInflater)
            setContentView(binding.root)

            supportActionBar?.setDisplayShowTitleEnabled(false)

            var userId = ""
            val preferences = getSharedPreferences("login", MODE_PRIVATE)
            val username = preferences.getString("username", userId)

        binding.insertProduct.setOnClickListener {
            val id = binding.id.text.toString()
            val password = binding.password.text.toString()
            val name = binding.name.text.toString()
            val phone = binding.phone.text.toString()
            val address = binding.address.text.toString()
            val money = 0
            val kit = "X"
            val vip = binding.vip.text.toString()

            val networkService = (applicationContext as MyApplication).networkService

            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }

                val token = task.result

                val user = User(id,password, name, phone, address, money, vip, kit, token)
                val csrfCall = networkService.getCsrfToken()

                csrfCall.enqueue(object : Callback<CsrfToken> {
                    override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                        val csrfToken = response.body()?.token
                        val userCall = networkService.newLogin(csrfToken, user)

                        userCall.enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>, response: Response<User>) {
                                val intent = Intent(this@InsertUser, LoginAdmin::class.java)
                                startActivity(intent)
                                Toast.makeText(this@InsertUser,"회원이 등록 되었습니다.", Toast.LENGTH_SHORT).show()

                            }

                            override fun onFailure(call: Call<User>, t: Throwable) {
                                call.cancel()
                            }

                        })
                    }

                    override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                        call.cancel()
                    }
                })
            })
        }

        binding.vip.setOnClickListener{
            val items = arrayOf("일반 회원", "VIP", "VVIP")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("등급 선택")
                .setItems(items) { dialog, which ->
                    val selectedValue = items[which]
                    binding.vip.text = selectedValue
                }

            val dialog = builder.create()
            dialog.show()
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