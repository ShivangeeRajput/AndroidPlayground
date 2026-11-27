package com.example.androidplayground.timer.ui

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplayground.timer.TimerEngine

/** WITHOUT service: survives rotation, stops when the screen is finished or the process dies. */
class TimerViewModel : ViewModel() {
    private val engine = TimerEngine(viewModelScope, SystemClock::elapsedRealtime)
    val state = engine.state

    fun start() = engine.start()
    fun pause() = engine.pause()
    fun reset() = engine.reset()
}
