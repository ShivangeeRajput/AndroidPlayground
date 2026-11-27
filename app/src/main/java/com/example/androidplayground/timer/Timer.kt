package com.example.androidplayground.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerState(val seconds: Int = 60, val isRunning: Boolean = false) {
    val text get() = "%02d:%02d".format(seconds / 60, seconds % 60)
}

// Countdown ka logic. ViewModel aur Service dono isi ko use karte hain
class Timer(private val scope: CoroutineScope) {
    private val _state = MutableStateFlow(TimerState())
    val state = _state.asStateFlow()

    private var job: Job? = null

    fun start() {
        if (job?.isActive == true) return // double start nahi
        if (_state.value.seconds == 0) _state.value = TimerState()
        _state.update { it.copy(isRunning = true) }
        job = scope.launch {
            while (_state.value.seconds > 0) {
                delay(1_000)
                _state.update { it.copy(seconds = it.seconds - 1) }
            }
            _state.update { it.copy(isRunning = false) }
        }
    }

    fun pause() {
        job?.cancel()
        _state.update { it.copy(isRunning = false) }
    }

    fun reset() {
        job?.cancel()
        _state.value = TimerState()
    }
}
