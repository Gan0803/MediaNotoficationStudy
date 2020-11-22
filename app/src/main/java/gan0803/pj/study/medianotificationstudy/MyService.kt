package gan0803.pj.study.medianotificationstudy

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

class MyService : Service(), MyReceiver.IMyCallback {
    companion object {
        val TAG: String = this::class.java.simpleName
        const val ONGOING_NOTIFICATION_ID = 1
        const val CHANNEL_ID = "NotificationStudy"
        const val CHANNEL_NAME = "NotificationStudy"
    }

    private lateinit var myReceiver: MyReceiver
    private var isRunning = true

    private val channelId by lazy {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(this, CHANNEL_ID, CHANNEL_NAME)
        } else {
            // If earlier version channel ID is not used
            ""
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(
        context: Context,
        channelId: String,
        channelName: String
    ): String {
        val channel = NotificationChannel(
            channelId,
            channelName, NotificationManager.IMPORTANCE_NONE
        )
        channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(channel)
        return channelId
    }

    private fun init() {
        myReceiver = MyReceiver()
        myReceiver.registerCallback(this)
        val filter = IntentFilter().apply {
            addAction(MyReceiver.ACTION_RUN)
            addAction(MyReceiver.ACTION_STOP)
        }
        application.registerReceiver(myReceiver, filter)
    }

    override fun onCreate() {
        super.onCreate()
        init()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(ONGOING_NOTIFICATION_ID, buildNotification())
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onDestroy() {
        application.unregisterReceiver(myReceiver)
        super.onDestroy()
    }

    private fun buildNotification(): Notification {
        return MyNotificationBuilder().build(this, isRunning, channelId)
    }

    override fun onReceiveRun() {
        Log.d(TAG, "onReceiveRun")
        isRunning = true
        // Update notification
        with(NotificationManagerCompat.from(this)) {
            notify(ONGOING_NOTIFICATION_ID, buildNotification())
        }
    }

    override fun onReceiveStop() {
        Log.d(TAG, "onReceiveStop")
        isRunning = false
        // Update notification
        with(NotificationManagerCompat.from(this)) {
            notify(ONGOING_NOTIFICATION_ID, buildNotification())
        }
    }
}