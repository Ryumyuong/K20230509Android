package com.example.test18

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.example.test18.databinding.ActivityMain2Binding
import com.example.test18.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {
    lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

//        Glide.with(this).load(R.drawable.bread5).into(binding.resultView)

        //파일에서 선택한 이미지 출력
        val requestLauncher = registerForActivityResult(

            ActivityResultContracts.StartActivityForResult()) {
                Glide.with(this).load(it.data?.data).into(binding.resultView)
            }
        val intent = Intent(Intent.ACTION_PICK, Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        requestLauncher.launch(intent)
    }
}