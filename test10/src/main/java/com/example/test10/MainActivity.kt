package com.example.test10

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.test10.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {


    @RequiresApi(Build.VERSION_CODES.R)
    fun showTest() {
        val toast = Toast.makeText(this, "메세지 내용", Toast.LENGTH_SHORT)
        toast.addCallback(
            @RequiresApi(Build.VERSION_CODES.R)
            object : Toast.Callback() {
                override fun onToastHidden() {
                    super.onToastHidden()
                    Log.d("lmj", "toast hidden, 숨겨진 후 추가 기능 동작")
                }

                override fun onToastShown() {
                    super.onToastShown()
                    Log.d("lmj", "toast shown, 보여진 후 추가 기능 동작")
                }
            })
        toast.show()
    }





    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*결과값을 엑티비티에 등록
        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Log.d("lmj", "승인됨")
            } else {
                Log.d("lmj", "승인 안됨")
            }
        }

        val status = ContextCompat.checkSelfPermission(this,"android.permission.ACCESS_FINE_LOCATION")

        if(status == PackageManager.PERMISSION_GRANTED) {
            Log.d("lmj", "승인됨2")
        } else {
            requestPermissionLauncher.launch("android.permission.ACCESS_FINE_LOCATION")
        }*/
        binding.btnTime.setOnClickListener { 
            TimePickerDialog(this,
                { _, hourOfDay, minute ->
                    Log.d("lmj", "hour : $hourOfDay, min : $minute")
                    Toast.makeText(this@MainActivity, "${hourOfDay}시 ${minute}분", Toast.LENGTH_SHORT).show()
                },15,0,true).show()
        }

        binding.btnDate.setOnClickListener {

            DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    Log.d("lmj", "year(년도) : $year, month : ${month+1}, day : $dayOfMonth")
                    Toast.makeText(this@MainActivity,"${year}년 ${month+1}월 ${dayOfMonth}일", Toast.LENGTH_SHORT).show()
                }
//                월은 0~11까지 선택
            }, 2020,5,15).show()
        }

        /*binding.btn1.setOnClickListener {
            toast.show()

            Toast.makeText(this, "토스트 출력 방법2", Toast.LENGTH_SHORT).show()
        }*/
        showTest()

            }



}
