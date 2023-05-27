package com.example.test10

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent

import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.test10.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
//      알림 설정
        binding.btnChannel.setOnClickListener {
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder : NotificationCompat.Builder

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channelId = "one-channel"
                val channelName = "My Channel One"
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                )

                channel.description = "My Channel 테스트 중 230515"
                // 알림 확인을 하지 않은 정보의 갯수 표시
                channel.setShowBadge(true)

                //소리
                val soundUri : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val audioAttr = AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM).build()

                channel.setSound(soundUri, audioAttr)

                // 알림시 LED 깜빡임
                channel.enableLights(true)
                channel.lightColor = Color.RED

                // 진동
                channel.enableVibration(true)
                channel.vibrationPattern = longArrayOf(100, 200, 100, 200)

                //등록
                manager.createNotificationChannel(channel)
                builder = NotificationCompat.Builder(this@MainActivity2, channelId)
            } else {
                builder = NotificationCompat.Builder(this@MainActivity2)
            }

            builder.setSmallIcon(android.R.drawable.ic_notification_overlay)
            builder.setWhen(System.currentTimeMillis())

            builder.setContentTitle("임시제목 2")
            builder.setContentText("전달할 임시 메세지 내용")

            builder.setAutoCancel(false)

            builder.setOngoing(true)

//            intent -> 시스템에 메세지를 전달하는 도구, 화면간의 전환, 데이터 전달
            val intent = Intent(this@MainActivity2, DetailActivity::class.java)

            // 요청번호 10번 옵션, 깃발을 이용해서 상태 표기
            val pendingIntent = PendingIntent.getActivity(this@MainActivity2, 10,intent, PendingIntent.FLAG_IMMUTABLE)

            val actionIntent = Intent(this@MainActivity2, OneReceiver::class.java)
            val actionPendingIntent = PendingIntent.getBroadcast(this@MainActivity2, 20,actionIntent,PendingIntent.FLAG_IMMUTABLE)
            builder.addAction(
                NotificationCompat.Action.Builder(
                    android.R.drawable.stat_notify_more, "Action 제목입니다.", actionPendingIntent
                ).build()
            )

            val KEY_TEXT_REPLY = "key_text_reply"
            var replyLabel: String = "답장 테스트"
            var remoteInput : RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run {
                setLabel(replyLabel)
                build()
            }

            val replyIntent = Intent(this@MainActivity2, ReplyReceiver::class.java)
            val replyPendingIntent = PendingIntent.getBroadcast(this@MainActivity2, 30, replyIntent, PendingIntent.FLAG_MUTABLE)
            builder.addAction(
                NotificationCompat.Action.Builder(
                    android.R.drawable.arrow_down_float,"답장 테스트", replyPendingIntent
                ).addRemoteInput(remoteInput).build()
            )

//            builder.setContentIntent(pendingIntent)
            builder.setAutoCancel(true)
            manager.notify(11,builder.build())
        }
    }


}