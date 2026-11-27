package com.example.androidplayground.timer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimerTest {

    @Test
    fun `pause stops counting and start resumes till zero`() = runTest {
        val timer = Timer(this)

        timer.start()
        advanceTimeBy(2_001)
        timer.pause()
        advanceTimeBy(10_000)
        assertEquals(58, timer.state.value.seconds)

        timer.start()
        advanceUntilIdle()
        assertEquals(TimerState(seconds = 0, isRunning = false), timer.state.value)
    }
}
