package com.example.androidplayground.notification

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.androidplayground.MainActivity
import com.example.androidplayground.timer.TimerService

object NotificationHelper {

    private const val FINISHED_CHANNEL_ID = "timer_channel"
    private const val RUNNING_CHANNEL_ID = "timer_running_channel"
    private const val FINISHED_NOTIFICATION_ID = 1
    const val RUNNING_NOTIFICATION_ID = 2

    // Android 8+ needs channels before any notification can show.
    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(
                NotificationChannel(
                    FINISHED_CHANNEL_ID, "Timer finished", NotificationManager.IMPORTANCE_HIGH
                ).apply { description = "Notifies when the timer finishes" }
            )
            // LOW = no sound on every update of the running notification.
            manager.createNotificationChannel(
                NotificationChannel(
                    RUNNING_CHANNEL_ID, "Timer running", NotificationManager.IMPORTANCE_LOW
                ).apply { description = "Shows the running timer" }
            )
        }
    }

    fun hasPermission(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

    private fun openAppIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        return PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    // Ongoing notification used by the foreground service. Required for startForeground().
    fun buildRunningNotification(context: Context, timeText: String): Notification {
        val pauseIntent = PendingIntent.getService(
            context, 1, TimerService.intentFor(context, TimerService.ACTION_PAUSE),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(context, RUNNING_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Timer running")
            .setContentText(timeText)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(openAppIntent(context))
            .addAction(0, "Pause", pauseIntent)
            .build()
    }

    fun updateRunningNotification(context: Context, timeText: String) {
        // Check permission right at the call site, and still guard against a revoked permission.
        if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
        ) return
        try {
            NotificationManagerCompat.from(context)
                .notify(RUNNING_NOTIFICATION_ID, buildRunningNotification(context, timeText))
        } catch (e: SecurityException) {
            // Permission was revoked between the check and the call; nothing to show.
        }
    }

    fun showTimerFinished(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) return

        val notification = NotificationCompat.Builder(context, FINISHED_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Time's up")
            .setContentText("Your timer has finished")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(openAppIntent(context))
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(FINISHED_NOTIFICATION_ID, notification)
        } catch (e: SecurityException) {
            // Permission was revoked between the check and the call.
        }
    }
}
