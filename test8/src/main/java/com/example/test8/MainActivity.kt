package com.example.test8

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.MotionEvent
import androidx.constraintlayout.widget.ConstraintSet.Motion
import com.example.test8.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


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