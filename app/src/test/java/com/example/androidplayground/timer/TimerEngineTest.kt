package com.example.androidplayground.timer

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TimerEngineTest {

    @Test
    fun `pause keeps remaining time and start resumes to zero`() = runTest {
        val engine = TimerEngine(this, now = { testScheduler.currentTime }, totalMillis = 3_000)

        engine.start()
        advanceTimeBy(1_001)
        engine.pause()
        assertEquals(2_000, engine.state.value.remainingMillis)

        advanceTimeBy(5_000) // time passes while paused
        assertEquals(2_000, engine.state.value.remainingMillis)

        engine.start()
        advanceUntilIdle()
        assertTrue(engine.state.value.isFinished)
        assertEquals("00:00", engine.state.value.text)
    }
}
