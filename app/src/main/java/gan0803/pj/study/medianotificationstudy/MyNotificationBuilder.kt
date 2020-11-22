package gan0803.pj.study.medianotificationstudy

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat as MediaNotificationCompat // Import by alias.

class MyNotificationBuilder {

    fun build(context: Context, isRunning: Boolean, channelId: String): Notification {
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Notification Study")
            .setContentText("Application is active.")
            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
            .setContentIntent(createPendingIntent(context))
            .setTicker("Application is active")
            .addAction(createMyAction(context, isRunning))
            .setStyle(
                MediaNotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0)
            )
            .build()
    }

    private fun createPendingIntent(context: Context): PendingIntent {
        return Intent(context, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(context, 0, notificationIntent, 0)
        }
    }

    private fun createMyAction(context: Context, isRunning: Boolean): NotificationCompat.Action {
        return if (isRunning) {
            val stopIntent = Intent().apply {
                action = MyReceiver.ACTION_STOP
            }
            NotificationCompat.Action(
                R.drawable.ic_baseline_directions_run_24,
                "Run",
                PendingIntent.getBroadcast(context, 0, stopIntent, 0)
            )
        } else {
            val runIntent = Intent().apply {
                action = MyReceiver.ACTION_RUN
            }
            NotificationCompat.Action(
                R.drawable.ic_baseline_emoji_people_24,
                "Stop",
                PendingIntent.getBroadcast(context, 0, runIntent, 0)
            )
        }
    }
}