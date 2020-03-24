package covid19.coronavirus.firebase.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import covid19.coronavirus.R
import covid19.coronavirus.feature.splash.SplashActivity

class CovidMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        showNotification(
            this@CovidMessagingService, remoteMessage.notification?.title,
            remoteMessage.notification?.body
        )
    }

    private fun showNotification(context: Context, title: String?, body: String?) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = System.currentTimeMillis().toInt()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        val intent = Intent(context, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setAutoCancel(true)
            .setContentTitle(title)
            .setColor(ContextCompat.getColor(applicationContext, R.color.redAccent))
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationId, mBuilder.build())
    }

    companion object {
        private const val CHANNEL_ID = "Messages"
        private const val CHANNEL_NAME = "Messages"
    }
}