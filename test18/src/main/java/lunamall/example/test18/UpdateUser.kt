package lunamall.example.test18


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import lunamall.example.test18.databinding.ActivityUpdateUserBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.User
import lunamall.example.test18.model.UserList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateUser : AppCompatActivity() {
    lateinit var binding: ActivityUpdateUserBinding
    private var money :Int?= 0
    private var kit :String?= ""
    private var code :String?= ""

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityUpdateUserBinding.inflate(layoutInflater)
            setContentView(binding.root)

            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false)
            binding.toolbar.title = "회원 수정"

            val userId = intent.getStringExtra("userId")

            val networkService = (applicationContext as MyApplication).networkService

            val userCall = networkService.user(userId)

            userCall.enqueue(object : Callback<UserList> {
                override fun onResponse(call: Call<UserList>, response: Response<UserList>) {
                    val user = response.body()?.items
                    val name = user?.get(0)?.username

                    money = user?.get(0)?.money
                    kit = user?.get(0)?.kit
                    code = user?.get(0)?.code

                    binding.id.text = userId
                    binding.name.text = name

                }

                override fun onFailure(call: Call<UserList>, t: Throwable) {
                    Log.d("lmj", "실패토큰 : ${t.message}")
                }
            })




        binding.updateProduct.setOnClickListener {

            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token

                    val user = User(binding.id.text.toString() ,binding.password.text.toString(),binding.name.text.toString(),binding.phone.text.toString(),
                        binding.address.text.toString(),money,binding.vip.text.toString(),kit,code)

                    val updateCall = networkService.updateUser(csrfToken, user, userId)

                    updateCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            val intent = Intent(this@UpdateUser, UserListActivity::class.java)
                            startActivity(intent)
                            Toast.makeText(this@UpdateUser,"회원수정이 완료되었습니다.",Toast.LENGTH_SHORT).show()

                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {

                        }
                    })


                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    Log.d("lmj", "실패토큰 : ${t.message}")
                }
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



    }
}