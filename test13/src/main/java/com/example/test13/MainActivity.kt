package com.example.test13

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.test13.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    //최초 실행 시 binding이 늦게 할당이 되어서 전역처럼 사용 가능
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn4.setOnClickListener {
            val intent = Intent()
            intent.action = "Intent.ACTION_VIEW"
            intent.data = Uri.parse("http://google.com")
            startActivity(intent)
            // 구글 지도 보는 속성
            /*val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:37.7749,127.4194"))
            intent.setPackage("com.google.android.apps.maps")
            startActivity(intent)*/
        }

        binding.btn1.setOnClickListener {
            val intent : Intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("data1", "류명조")
            intent.putExtra("data2", 518)
//            startActivity(intent)
            startActivityForResult(intent, 10)
        }

        val requestLauncher : ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultData = it.data?.getStringExtra("result2")
            binding.result2MainViewText.text = resultData
        }

        binding.btn3.setOnClickListener {
            val intent : Intent = Intent(this@MainActivity, DetailActivity::class.java)
            intent.putExtra("data1", "후처리 방법 2")
            intent.putExtra("data2", 777)
            requestLauncher.launch(intent)

        }
    }
    //가급적 사용 자제
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 10 && resultCode == Activity.RESULT_OK) {
            val result = data?.getStringExtra("result")
            binding.resultMainViewText.text = result
        }
    }
}