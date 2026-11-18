package com.example.androidplayground.timer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import com.example.androidplayground.notification.NotificationHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerService : Service() {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private var timerJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_PAUSE -> pause()
            ACTION_RESET -> reset()
        }
        // If the system kills us, don't restart with a null intent; the state is in-memory only.
        return START_NOT_STICKY
    }

    private fun start() {
        // startForegroundService() requires startForeground() within ~5s, so call it first.
        NotificationHelper.createChannels(this)
        val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
        } else 0
        ServiceCompat.startForeground(
            this,
            NotificationHelper.RUNNING_NOTIFICATION_ID,
            NotificationHelper.buildRunningNotification(this, "Starting…"),
            type
        )

        if (timerJob?.isActive == true) return

        val current = TimerRepository.state.value
        val startFrom = if (current.isFinished || current.timeLeftMillis <= 0) {
            TimerRepository.TOTAL_MILLIS
        } else current.timeLeftMillis

        // Use a wall-clock end time instead of counting ticks, so the timer doesn't drift.
        val endTime = SystemClock.elapsedRealtime() + startFrom
        TimerRepository.update { it.copy(timeLeftMillis = startFrom, isRunning = true, isFinished = false) }

        timerJob = scope.launch {
            while (true) {
                val remaining = (endTime - SystemClock.elapsedRealtime()).coerceAtLeast(0L)
                // Round up to whole seconds so the display goes 60, 59, 58 ... 0
                val shown = ((remaining + 999) / 1000) * 1000
                TimerRepository.update { it.copy(timeLeftMillis = shown) }
                if (remaining == 0L) break
                NotificationHelper.updateRunningNotification(this@TimerService, format(shown))
                delay(remaining % 1000L + 1L)
            }
            finish()
        }
    }

    private fun pause() {
        timerJob?.cancel()
        TimerRepository.update { it.copy(isRunning = false) }
        stopForegroundAndSelf()
    }

    private fun reset() {
        timerJob?.cancel()
        TimerRepository.update { TimerUiState() }
        stopForegroundAndSelf()
    }

    private fun finish() {
        TimerRepository.update { it.copy(timeLeftMillis = 0, isRunning = false, isFinished = true) }
        stopForegroundAndSelf()
        NotificationHelper.showTimerFinished(this)
    }

    private fun stopForegroundAndSelf() {
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    override fun onDestroy() {
        scope.cancel()   // no leaked coroutines
        super.onDestroy()
    }

    private fun format(millis: Long): String {
        val s = millis / 1000
        return "%02d:%02d".format(s / 60, s % 60)
    }

    companion object {
        const val ACTION_START = "com.example.androidplayground.timer.START"
        const val ACTION_PAUSE = "com.example.androidplayground.timer.PAUSE"
        const val ACTION_RESET = "com.example.androidplayground.timer.RESET"

        fun intentFor(context: Context, action: String) =
            Intent(context, TimerService::class.java).setAction(action)

        fun start(context: Context) =
            ContextCompat.startForegroundService(context, intentFor(context, ACTION_START))

        fun pause(context: Context) {
            context.startService(intentFor(context, ACTION_PAUSE))
        }

        fun reset(context: Context) {
            context.startService(intentFor(context, ACTION_RESET))
        }
    }
}
