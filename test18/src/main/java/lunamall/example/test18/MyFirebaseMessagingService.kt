package lunamall.example.test18

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val GROUP_KEY = "lunamall_group"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        Log.d("lmj", "fcm title title : $title")
        Log.d("lmj", "fcm body body : $body")

        showNotification(title, body)
    }

    private fun showNotification(title: String?, body: String?) {
        val channelId = "lunamall"
        val channelName = "루나몰"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(this, LunaActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationId = System.currentTimeMillis().toInt()

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setGroup(GROUP_KEY)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val existingSummaryNotification = notificationManager.activeNotifications
            .find { it.id == 0 && it.notification.group == GROUP_KEY }

        if (existingSummaryNotification == null) {
            val summaryNotification = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.logo)
                .setGroup(GROUP_KEY)
                .setGroupSummary(true)
                .setContentIntent(pendingIntent)
                .build()

            notificationManager.notify(0, summaryNotification)
        }

        notificationManager.notify(notificationId, notificationBuilder.build())
    }
}
