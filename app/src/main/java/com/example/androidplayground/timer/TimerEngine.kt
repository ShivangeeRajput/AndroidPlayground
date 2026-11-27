package com.example.androidplayground.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TimerState(val remainingMillis: Long, val isRunning: Boolean = false) {
    val isFinished get() = remainingMillis == 0L

    /** Rounded up, so the display goes 01:00, 00:59 ... 00:00 */
    val text: String
        get() {
            val seconds = (remainingMillis + 999) / 1000
            return "%02d:%02d".format(seconds / 60, seconds % 60)
        }
}

/**
 * Pure Kotlin countdown. Knows nothing about Android, so the ViewModel, the Service
 * and unit tests can all use it. [now] must be a monotonic clock (elapsedRealtime).
 */
class TimerEngine(
    private val scope: CoroutineScope,
    private val now: () -> Long,
    private val totalMillis: Long = 60_000,
) {
    private val _state = MutableStateFlow(TimerState(totalMillis))
    val state: StateFlow<TimerState> = _state.asStateFlow()

    private var job: Job? = null

    fun start() {
        if (job?.isActive == true) return
        val startFrom = _state.value.remainingMillis.takeIf { it > 0 } ?: totalMillis
        // Compare against an end time instead of subtracting 1s per tick, so it never drifts.
        val endTime = now() + startFrom
        _state.value = TimerState(startFrom, isRunning = true)
        job = scope.launch {
            var remaining = startFrom
            while (remaining > 0) {
                delay(minOf(remaining, TICK_MILLIS))
                remaining = (endTime - now()).coerceAtLeast(0)
                _state.value = TimerState(remaining, isRunning = remaining > 0)
            }
        }
    }

    fun pause() {
        job?.cancel()
        _state.update { it.copy(isRunning = false) }
    }

    fun reset() {
        job?.cancel()
        _state.value = TimerState(totalMillis)
    }

    private companion object {
        const val TICK_MILLIS = 200L
    }
}
