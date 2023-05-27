package com.example.test8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.CompoundButton
import androidx.constraintlayout.widget.ConstraintSet.Motion
import com.example.test8.databinding.ActivityMainBinding

/*class MainActivity : AppCompatActivity(),CompoundButton.OnCheckedChangeListener {

    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Log.d("lmj", "체크박스 클릭")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.check1.setOnCheckedChangeListener(this)

    }*/

class MyEventHandler : CompoundButton.OnCheckedChangeListener {
    override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
        Log.d("lmj", "클래스로 구현 체크박스 클릭")
    }

}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//      binding.check1.setOnCheckedChangeListener(MyEventHandler())

        //제일 쉽다
        binding.check1.setOnCheckedChangeListener{
            a, b -> Log.d("lmj", "방법 3 SAM 기법 구현 : 체크박스 클릭")
        }

        binding.btn1.setOnLongClickListener{
            Log.d("lmj", "방법 3 SAM 기법 구현 : 롱클릭")
            true
        }
    }
    override fun onTouchEvent(event: MotionEvent?):Boolean {
        when(event?.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("lmj", "touch down 좌표 x : ${event.x}, 좌표 rawx : ${event.rawX}, 좌표 y : ${event.y}")
            }
            MotionEvent.ACTION_UP -> {
                Log.d("lmj", "touch up 좌표 x : ${event.x}, 좌표 rawx : ${event.rawX}, 좌표 y : ${event.y}")
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        when(keyCode) {
            KeyEvent.KEYCODE_BACK -> Log.d("lmj", "백키 누름")
            KeyEvent.KEYCODE_VOLUME_UP -> Log.d("lmj", "볼륨업 누름")
            KeyEvent.KEYCODE_VOLUME_DOWN -> Log.d("lmj", "볼륨다운 누름")
        }
 //       Log.d("lmj", "onKeyDown 실행")
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
 //       Log.d("lmj", "onKeyUp 실행")
        return super.onKeyUp(keyCode, event)
    }
}