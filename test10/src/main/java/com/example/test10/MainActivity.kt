package com.example.test10

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.DatePicker
import android.widget.MediaController
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.test10.databinding.ActivityMainBinding



class MainActivity : AppCompatActivity() {

    val eventHandler = object : DialogInterface.OnClickListener {
        override fun onClick(dialog: DialogInterface?, which: Int) {
            if(which == DialogInterface.BUTTON_POSITIVE) {
                Toast.makeText(this@MainActivity, "확인시 토스트 띄우기", Toast.LENGTH_SHORT).show()
            } else {
                if(which == DialogInterface.BUTTON_NEGATIVE) {
                    Toast.makeText(this@MainActivity, "취소시 토스트 띄우기", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


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

        binding.btnVideo.setOnClickListener {
            val videoFile:Uri = Uri.parse("android.resource://$packageName/raw/testvideo")
            /*val player : MediaPlayer = MediaPlayer.create(this, R.raw.testvideo)
            player.start()*/

            val mc = MediaController(this)

            binding.videoView.setMediaController(mc)
            binding.videoView.setVideoPath(videoFile.toString())
            binding.videoView.start()
        }

        binding.btnSound.setOnClickListener {
            val notification : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
            ringtone.play()
        }

        binding.btnCheck.setOnClickListener {
            val items = arrayOf<String>("두루치기", "된장찌개", "밀면", "칼국수")
            AlertDialog.Builder(this@MainActivity).run {
                setTitle("checkbox alert 다이얼로그")
                setIcon(android.R.drawable.ic_dialog_info)
                setMultiChoiceItems(
                    items,
                    booleanArrayOf(true, false, false, false),
                    object : DialogInterface.OnMultiChoiceClickListener {
                        override fun onClick(
                            dialog: DialogInterface?,
                            which: Int,
                            isChecked: Boolean
                        ) {
                            Log.d(
                                "lmj",
                                "선택한 점심메뉴 : ${items[which]}이 ${
                                    if (isChecked) "선택됨"
                                    else "선택해제됨"
                                }"
                            )
                        }
                    }
                )
                setPositiveButton("닫기", null)
                setCancelable(true)
                show()
            }.setCanceledOnTouchOutside(true)
        }

        binding.btnMenu.setOnClickListener {
            val items = arrayOf<String>("두루치기", "된장찌개", "밀면", "칼국수")

            AlertDialog.Builder(this@MainActivity).run {
                setTitle("메뉴 alert 다이얼로그")
                setIcon(android.R.drawable.ic_dialog_info)
                setItems(
                    items,
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            Log.d("lmj", "선택한 점심메뉴 : ${items[which]}")
                        }
                    }
                )
                setPositiveButton("닫기", null)
                show()
            }
        }

        binding.btnAlert.setOnClickListener {
            AlertDialog.Builder(this@MainActivity).run {
                setTitle("테스트 제목")
                setIcon(android.R.drawable.ic_dialog_info)
                setMessage("토스트 메세지 띄울까요?")
                setPositiveButton("확인",eventHandler)
                setNegativeButton("취소", eventHandler)
                show()
            }
        }

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
        /*showTest()*/

            }



}
