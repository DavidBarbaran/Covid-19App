package covid19.coronavirus.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import covid19.coronavirus.BuildConfig
import covid19.coronavirus.R
import covid19.coronavirus.firebase.fcm.CovidMessagingService
import java.io.File

class DownloadService : Service() {

    private var destination = ""
    private var downloadId: Long = -1
    private var notificationBuilder: NotificationCompat.Builder? = null

    private val onCompleteDownloadBroadcast = object : BroadcastReceiver() {
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId == id) {
                showNotificationDownloaded(destination)
                publishResult(destination)
                stopForeground(true)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        showNotificationDownload()
        val downloadUrl = intent?.extras?.getString(DOWNLOAD_FILE, "") ?: ""

        application.registerReceiver(
            onCompleteDownloadBroadcast,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )
        download(downloadUrl)
        return START_NOT_STICKY
    }

    private fun download(downloadUrl: String) {
        destination =
            applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + FILE_NAME

        val uriFile = Uri.parse("$FILE_BASE_PATH$destination")
        val file = File(destination)
        if (file.exists()) file.delete()

        val requestDownload = DownloadManager.Request(downloadUrl.toUri())
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN)
            .setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
            )
            .setMimeType(MIME_TYPE)
            .setDestinationUri(uriFile)

        val downloadManager =
            applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadId = downloadManager.enqueue(requestDownload)
    }

    private fun showNotificationDownload() {
        val title = applicationContext.getString(R.string.notify_download_app)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationId = 1

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                CovidMessagingService.CHANNEL_ID_UPDATE,
                CovidMessagingService.CHANNEL_NAME_UPDATE,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        notificationBuilder =
            NotificationCompat.Builder(applicationContext, CovidMessagingService.CHANNEL_ID_UPDATE)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(applicationContext, R.color.redAccent))
                .setOngoing(true)
                .setProgress(100, 0, true)
                .setSmallIcon(R.drawable.ic_notification)

        startForeground(notificationId, notificationBuilder?.build())
    }

    private fun showNotificationDownloaded(destination: String) {

        val title = applicationContext.getString(R.string.open_app_title)
        val body = applicationContext.getString(R.string.open_app_notify_message)

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.cancel(1)

        val notificationId = 2

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                CovidMessagingService.CHANNEL_ID_UPDATE,
                CovidMessagingService.CHANNEL_NAME_UPDATE,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        val intent = getIntentInstallNewVersion(destination)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

        notificationBuilder?.apply {
            setAutoCancel(true)
                .setContentTitle(title)
                .setColor(ContextCompat.getColor(applicationContext, R.color.redAccent))
                .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                .setContentText(body)
                .setOngoing(false)
                .setProgress(0, 0, false)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
        }
        notificationManager.notify(notificationId, notificationBuilder?.build())
    }

    private fun getIntentInstallNewVersion(destination: String): Intent {
        val uriFile = Uri.parse("$FILE_BASE_PATH$destination")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                applicationContext,
                BuildConfig.APPLICATION_ID + PROVIDER_PATH,
                File(destination)
            )
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            install.data = contentUri
            return install
        } else {
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            install.setDataAndType(
                uriFile,
                MIME_TYPE
            )
            return install
        }
    }

    private fun publishResult(destination: String) {
        val intent = Intent(RECEIVER_DOWNLOAD_SERVICE)
        intent.putExtra(DESTINATION, destination)
        sendBroadcast(intent)
    }

    companion object {
        private const val FILE_NAME = "Covid19App.apk"
        const val MIME_TYPE = "application/vnd.android.package-archive"
        const val FILE_BASE_PATH = "file://"
        const val PROVIDER_PATH = ".provider"

        const val DESTINATION = "destination"
        const val DOWNLOAD_FILE = "download_file"
        const val RECEIVER_DOWNLOAD_SERVICE = "covid19.coronavirus.android.service.receiver"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}