package lunamall.example.test18

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import lunamall.example.test18.databinding.ActivityInsertProductBinding
import lunamall.example.test18.model.CsrfToken
import lunamall.example.test18.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class InsertProduct : AppCompatActivity() {
    private val REQUEST_IMAGE_PICK = 1
    private val REQUEST_PERMISSION = 2
    private var fileName: String = ""

    lateinit var binding: ActivityInsertProductBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "물품 등록"

        binding.category.setOnClickListener{
            val items = arrayOf("전자", "생활가전", "식음료", "운동", "여가문화", "여행", "업무", "VVIP", "웰컴키트")

            val builder = AlertDialog.Builder(this)
            builder.setTitle("카테고리 선택")
                .setItems(items) { dialog, which ->
                    val selectedValue = items[which]
                    binding.category.text = selectedValue

                    dialog.dismiss()
                }

            val dialog = builder.create()
            dialog.show()
        }

        binding.imgbtn.setOnClickListener{
            if (checkPermission()) {
                openGallery()
            } else {
                requestPermission()
            }
        }

        binding.insertProduct.setOnClickListener {
            val category = binding.category.text.toString()
            val name = binding.name.text.toString()
            val priceStr = binding.price.text.toString()
            val price = if (priceStr.isNotEmpty()) {
                priceStr.toInt()
            } else {
                0
            }

            val description = binding.description.text.toString()
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
                            val intent = Intent(this@InsertProduct, MainActivity::class.java)
                            startActivity(intent)
                        }

                        override fun onFailure(call: Call<Unit>, t: Throwable) {
                            call.cancel()
                        }

                    })
                }

                override fun onFailure(call: Call<CsrfToken>, t: Throwable) {
                    call.cancel()
                }
            })
        }

    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_MEDIA_IMAGES
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
            REQUEST_PERMISSION
        )
    }

    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, REQUEST_IMAGE_PICK)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                Toast.makeText(this,"권한이 거부되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            val selectedBitmap: Bitmap =
                MediaStore.Images.Media.getBitmap(contentResolver, data.data)

            binding.image.text = "이미지가 선택되었습니다."
            val base64Image: String = bitmapToBase64(selectedBitmap)
            getBase64Image(base64Image)

        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    private fun getBase64Image(base64Image: String) {
        fileName = base64Image
    }
}