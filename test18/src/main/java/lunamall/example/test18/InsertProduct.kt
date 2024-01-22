package lunamall.example.test18

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import lunamall.example.test18.databinding.ActivityInsertProductBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.Product
import lunamall.example.test18.model.UserList
import lunamall.example.test18.recycler.MyUserAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertProduct : AppCompatActivity() {
    lateinit var binding: ActivityInsertProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "물품 등록"

        binding.category.setOnClickListener{
            val items = arrayOf("전자/기기", "생활가전", "식음료", "운동", "여가문화", "여행", "업무", "VVIP", "웰컴키트")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("카테고리 선택")
                .setItems(items) { dialog, which ->
                    val selectedValue = items[which]
                    binding.category.text = selectedValue
                }

            val dialog = builder.create()
            dialog.show()
        }

        binding.insertProduct.setOnClickListener {
            val category = binding.category.text.toString()
            val name = binding.name.text.toString()
            val price = binding.price.text.toString().toInt()
            val description = binding.description.text.toString()
            val fileName = ""
            val product = Product(category,name, price, description, fileName)

            val networkService = (applicationContext as MyApplication).networkService

            val csrfCall = networkService.getCsrfToken()

            csrfCall.enqueue(object : Callback<CsrfToken> {
                override fun onResponse(call: Call<CsrfToken>, response: Response<CsrfToken>) {
                    val csrfToken = response.body()?.token

                    val insertProductCall = networkService.insertProduct(csrfToken, product)

                    insertProductCall.enqueue(object : Callback<Unit> {
                        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                            Toast.makeText(this@InsertProduct,"물품이 등록되었습니다.", Toast.LENGTH_LONG).show()
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            Log.d("lmj", "실패 내용 : ${t.message}")
                            call.cancel()
                        }

                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    Log.d("lmj", "실패토큰 : ${t.message}")
                }
            })
        }

    }
}