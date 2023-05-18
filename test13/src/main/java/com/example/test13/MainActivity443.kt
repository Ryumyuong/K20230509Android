package com.example.test13

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import com.example.test13.databinding.ActivityMain443Binding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlin.concurrent.thread
import kotlin.system.measureTimeMillis

class MainActivity443 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain443Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
//            var sum = 0L
//            var time = measureTimeMillis {
//                for(i in 1..20_000_000_000){
//                    sum += i
//                }
//            }
//            Log.d("lmj","time : $time")
//            binding.resultView.text = "sum : $sum"

//            핸들러 스레드로 비동기처리
//            val handler=object: Handler(){
//                override fun handleMessage(msg: Message) {
//                    super.handleMessage(msg)
//                    binding.resultView.text = "sum : ${msg.arg1}"
//                }
//            }
//
//            thread {
//                var sum = 0L
//                var time = measureTimeMillis {
//                    for (i in 1..7_000_000_000) {
//                        sum += i
//                    }
//                    val message = Message()
//                    message.arg1=sum.toInt()
//                    handler.sendMessage(message)
//                }
//                Log.d("lmj", "time : $time")
//            }

//            코루틴으로 비동기 처리
            val channel = Channel<Int>()

            val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
            backgroundScope.launch {
                var sum = 0L
                var time = measureTimeMillis {
                    for (i in 1..2_000_000_000) {
                        sum += i
                    }
                }
                Log.d("lmj", "time : $time")
                channel.send(sum.toInt())
            }

            val mainScope= GlobalScope.launch(Dispatchers.Main) {
                channel.consumeEach {
                    binding.resultView.text = "sum : $it"
                }
            }


        }
    }
}