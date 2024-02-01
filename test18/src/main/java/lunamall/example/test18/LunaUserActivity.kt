package lunamall.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import lunamall.example.test18.databinding.ActivityLunaUserBinding
import lunamall.example.test18.model.CsrfToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LunaUserActivity : AppCompatActivity() {
    lateinit var binding: ActivityLunaUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLunaUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "루나 전환"

        binding.luna.text = intent.getIntExtra("luna",0).toString()

        binding.DoDo.text = intent.getIntExtra("dodo",0).toString()

        var userId = ""
        val preferences = getSharedPreferences("login", MODE_PRIVATE)
        val username = preferences.getString("username", userId)

        binding.addLuna.setOnClickListener {
            val dodo = binding.dodo.text.toString().toIntOrNull()
            val runa = binding.Luna.text.toString().toIntOrNull()
            if((dodo == null) and (runa == null)) {
                Toast.makeText(this,"계산하실 포인트를 입력하세요",Toast.LENGTH_LONG).show()
            }else {
                val luna = (dodo?.times(4) ?: 0) /1000
                val dod =  runa?.times(250) ?: 0
                val intent = Intent(this, LunaUserActivity::class.java)
                intent.putExtra("luna",luna)
                intent.putExtra("dodo",dod)
                startActivity(intent)
            }

        }

        binding.change.setOnClickListener {
            val luna = binding.lunaAdd.text.toString().toIntOrNull()

            if(luna != null) {
                val luna = binding.lunaAdd.text.toString()
                    val networkService = (applicationContext as MyApplication).networkService
                    val csrfCall = networkService.getCsrfToken()

                    csrfCall.enqueue(object : Callback<CsrfToken> {
                        override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                            val csrfToken = response.body()?.token

                            val notiTokenMyCall = networkService.notiLunaToken(csrfToken, username, luna)

                            notiTokenMyCall.enqueue(object : Callback<Unit> {
                                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                                    val intent = Intent(this@LunaUserActivity,LoginDetail::class.java)
                                    startActivity(intent)
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
            } else {
                Toast.makeText(this,"전환 신청할 루나를 입력하세요",Toast.LENGTH_LONG).show()
            }
        }


    }
}