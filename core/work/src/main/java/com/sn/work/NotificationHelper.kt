package com.sn.work

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "reminder_channel_id"
    private val NOTIFICATION_ID = 1

    fun createNotification(title: String, message: String) {
        createNotificationChannel()

        val icon =
            BitmapFactory.decodeResource(context.resources, com.sn.designsystem.R.drawable.note)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.sn.designsystem.R.drawable.note)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)

            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        NotificationManagerCompat.from(context).notify(NOTIFICATION_ID, notification)

    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_ID,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Reminder Channel Description"
        }
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}