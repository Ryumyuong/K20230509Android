package com.example.test17_4

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val pref = getSharedPreferences("inputPref", Context.MODE_PRIVATE)
        // 값을 설정하는 부분
        pref.edit().run {
            putString("test", "앱별 공유 프러퍼런스 테스트중, 값 부분")
            putString("name", "류명조, 공유 파일명(이름은 아무거나)은 : inputPref")
            commit()
        }
        // 값을 가져오는 부분
        val resultStr2 : String? = pref.getString("test","default")
        val resultStr3 : String? = pref.getString("name","default")
        val result3 = resultStr2.toString()
        val result4 = resultStr3.toString()
        Log.d("lmj","imgInfo result3 결과 : $result3")
        Log.d("lmj","imgInfo result4 결과 : $result4")
        Toast.makeText(this@MainActivity2, "${result3}, ${result4}", Toast.LENGTH_SHORT).show()
    }
}