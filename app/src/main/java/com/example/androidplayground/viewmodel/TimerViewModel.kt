package com.example.androidplayground.viewmodel

import android.os.CountDownTimer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TimerViewModel : ViewModel() {

    private val totalMillis = 60_000L          // 1 minute timer

    var timeLeftMillis by mutableStateOf(totalMillis)
        private set
    var isRunning by mutableStateOf(false)
        private set
    var isFinished by mutableStateOf(false)
        private set

    private var countDownTimer: CountDownTimer? = null

    fun start() {
        if (isRunning) return
        isFinished = false
        isRunning = true
        countDownTimer = object : CountDownTimer(timeLeftMillis, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftMillis = millisUntilFinished
            }

            override fun onFinish() {
                timeLeftMillis = 0
                isRunning = false
                isFinished = true      // Step 2: we'll fire the notification from here
            }
        }.start()
    }

    fun pause() {
        countDownTimer?.cancel()
        isRunning = false
    }

    fun reset() {
        countDownTimer?.cancel()
        timeLeftMillis = totalMillis
        isRunning = false
        isFinished = false
    }

    override fun onCleared() {
        countDownTimer?.cancel()   // avoid leaks when ViewModel dies
        super.onCleared()
    }
}
