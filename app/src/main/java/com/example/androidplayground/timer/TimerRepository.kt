package com.example.androidplayground.timer

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TimerUiState(
    val timeLeftMillis: Long = TimerRepository.TOTAL_MILLIS,
    val isRunning: Boolean = false,
    val isFinished: Boolean = false
)

/**
 * Single source of truth for the timer. The service writes to it, the UI only reads it,
 * so the screen and the service can never disagree.
 */
object TimerRepository {
    const val TOTAL_MILLIS = 60_000L

    private val _state = MutableStateFlow(TimerUiState())
    val state: StateFlow<TimerUiState> = _state.asStateFlow()

    fun update(block: (TimerUiState) -> TimerUiState) = _state.update(block)
}
