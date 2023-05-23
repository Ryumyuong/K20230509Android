package com.example.test17_4

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.test17_4.databinding.ActivityMainBinding
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var file : File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = getSharedPreferences("inputPref", Context.MODE_PRIVATE)
        // 값을 설정하는 부분
        /*pref.edit().run {
            putString("test", "앱별 공유 프러퍼런스 테스트중, 값 부분")
            putString("name", "류명조, 공유 파일명(이름은 아무거나)은 : inputPref")
            commit()
        }*/
        // 값을 가져오는 부분
        val resultStr2 : String? = pref.getString("test","default")
        val resultStr3 : String? = pref.getString("name","default")
        val result3 = resultStr2.toString()
        val result4 = resultStr3.toString()
        Log.d("lmj","imgInfo result3 결과 : $result3")
        Log.d("lmj","imgInfo result4 결과 : $result4")
        Toast.makeText(this@MainActivity, "${result3}, ${result4}", Toast.LENGTH_SHORT).show()

        val file2:File? = getExternalFilesDir(null)
        Log.d("lmj","getgetExternalFilesDir의 위치 : ${file2?.absolutePath}")

        binding.button1.setOnClickListener {
            file = File(filesDir, "test230522-1.txt")
            val writeStream: OutputStreamWriter = file.writer()
            writeStream.write("앱별 저장소에 파일 저장 테스트 내용")
            writeStream.flush()

//            openFileOutput("test.txt", Context.MODE_PRIVATE).use {
//                it.write("hello world!!".toByteArray())
//            }

        }
        binding.button2.setOnClickListener {
            val readStream: BufferedReader = file.reader().buffered()
            readStream.forEachLine {
                Log.d("lmj", "$it")
            }

//            openFileInput("test.txt").bufferedReader().forEachLine {
//                Log.d("lmj", "$it")
//            }
        }
    }
}