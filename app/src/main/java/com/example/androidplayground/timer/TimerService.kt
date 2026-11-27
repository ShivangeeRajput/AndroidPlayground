package com.example.androidplayground.timer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Keeps the process alive and shows a notification. The countdown itself is in a
 * singleton TimerEngine, which the screen observes directly.
 */
class TimerService : Service() {
    private val engine: TimerEngine by inject()
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val notifications by lazy { getSystemService(NotificationManager::class.java) }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notifications.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "Timer", NotificationManager.IMPORTANCE_LOW),
            )
        }
        scope.launch {
            // Update the notification only when the visible text changes.
            engine.state.map { it.text to it.isFinished }.distinctUntilChanged().collect { (text, finished) ->
                notifications.notify(NOTIFICATION_ID, notification(if (finished) "Time's up!" else text))
                if (finished) stop(removeNotification = false)
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // startForeground must be called within a few seconds of startForegroundService.
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        } else {
            0
        }
        engine.start()
        ServiceCompat.startForeground(this, NOTIFICATION_ID, notification(engine.state.value.text), type)
        return START_NOT_STICKY
    }

    private fun stop(removeNotification: Boolean) {
        val mode = if (removeNotification) ServiceCompat.STOP_FOREGROUND_REMOVE else ServiceCompat.STOP_FOREGROUND_DETACH
        ServiceCompat.stopForeground(this, mode)
        stopSelf()
    }

    private fun notification(text: String): Notification =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Timer")
            .setContentText(text)
            .setOnlyAlertOnce(true)
            .build()

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    companion object {
        private const val CHANNEL_ID = "timer"
        private const val NOTIFICATION_ID = 1

        fun start(context: Context) =
            ContextCompat.startForegroundService(context, Intent(context, TimerService::class.java))

        /** onDestroy runs, and a foreground notification is removed with the service. */
        fun stop(context: Context) {
            context.stopService(Intent(context, TimerService::class.java))
        }
    }
}
