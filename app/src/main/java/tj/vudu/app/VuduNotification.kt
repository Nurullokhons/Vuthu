package tj.vudu.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build

object VuduNotification {
    const val CHANNEL_ID = "vudu_status"
    const val NOTIFICATION_ID = 701
    const val ACTION_TOGGLE = "tj.vudu.app.TOGGLE"

    fun show(context: Context) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Состояние омовения", NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Постоянный переключатель состояния омовения"
                setShowBadge(false)
                enableVibration(false)
                lightColor = Color.GREEN
            }
            manager.createNotificationChannel(channel)
        }

        val hasWudu = VuduState.hasWudu(context)
        val toggleIntent = Intent(context, ToggleReceiver::class.java).setAction(ACTION_TOGGLE)
        val togglePending = PendingIntent.getBroadcast(
            context, 1, toggleIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val openIntent = PendingIntent.getActivity(
            context, 2, Intent(context, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(context)
        }
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle(if (hasWudu) "Вуду: омовение есть" else "Вуду: омовения нет")
            .setContentText("Нажми «Переключить», чтобы изменить состояние")
            .setContentIntent(openIntent)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .addAction(
                android.R.drawable.ic_menu_rotate,
                if (hasWudu) "Снять отметку" else "Отметить омовение",
                togglePending
            )
            .build()

        manager.notify(NOTIFICATION_ID, notification)
    }
}
