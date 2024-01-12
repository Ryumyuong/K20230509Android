package com.example.test18

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var notificationId = 0
    private val GROUP_KEY = "lunamall_group"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // 알림 메시지가 수신되었을 때 실행되는 부분
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        Log.d("lmj", "fcm title title : $title")
        Log.d("lmj", "fcm body body : $body")

        showNotification(title, body)
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "lunamall"
        val channelName = "루나몰"

        // Notification 채널 생성 (Android 8.0 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, LunaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        // NotificationCompat.Builder를 사용하여 알림 생성
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.profiler)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)

        // 알림을 표시
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId++, notificationBuilder.build())

        // 그룹 요약 알림 생성
        val summaryNotification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.profiler)
            .setGroup(GROUP_KEY)
            .setGroupSummary(true)
            .build()

        notificationManager.notify(0, summaryNotification)
    }
}