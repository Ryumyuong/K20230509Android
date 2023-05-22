package com.example.test17_4

import android.content.ContentUris
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.example.test17_4.databinding.ActivityMain546Binding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MainActivity546 : AppCompatActivity() {
    lateinit var binding: ActivityMain546Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain546Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {
            //파일 쓰기
            val file: File = File(getExternalFilesDir(null), "test.txt")
            val writeStream: OutputStreamWriter = file.writer()
            writeStream.write("hello world")
            writeStream.flush()
            // 파일 읽기
            val readStream: BufferedReader = file.reader().buffered()
            readStream.forEachLine {
                Log.d("lmj", "$it")
            }
        }
        binding.button2.setOnClickListener {
            //공용저장소...........
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
            )
            //공용 저장소에 있는 사진 이미지의 아이디와 파일명을 불러온다
            val cursor = contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection,
                null,
                null,
                null
            )
            cursor?.let {
                while (cursor.moveToNext()) {
                    Log.d("lmj", "_id : ${cursor.getLong(0)}, name : ${cursor.getString(1)}")
                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        cursor.getLong(0)
                    )

                    val resolver = applicationContext.contentResolver
                    resolver.openInputStream(contentUri).use { stream ->
                        // stream 객체에서 작업 수행
                        val option = BitmapFactory.Options()
                        option.inSampleSize = 10
                        val bitmap = BitmapFactory.decodeStream(stream, null, option)
                        binding.resultImageView.setImageBitmap(bitmap)
                    }
                }
            }
        }

    }
}