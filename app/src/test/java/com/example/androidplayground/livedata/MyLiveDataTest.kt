package com.example.androidplayground.livedata

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.Executor

private class TestOwner : LifecycleOwner {
    // createUnsafe: no main thread check, so it works in a JVM test
    private val registry = LifecycleRegistry.createUnsafe(this)
    override val lifecycle: Lifecycle get() = registry
    fun moveTo(state: Lifecycle.State) {
        registry.currentState = state
    }
}

class MyLiveDataTest {
    private val liveData = MyMutableLiveData<Int>(main = Executor { it.run() })
    private val owner = TestOwner()
    private val received = mutableListOf<Int>()

    @Test
    fun `stopped owner gets only the latest value when it starts`() {
        owner.moveTo(Lifecycle.State.CREATED)
        liveData.observe(owner) { received += it }
        liveData.setValue(1)
        liveData.setValue(2)
        assertEquals(emptyList<Int>(), received)

        owner.moveTo(Lifecycle.State.STARTED)
        assertEquals(listOf(2), received)
    }

    @Test
    fun `same value is not delivered again on restart`() {
        owner.moveTo(Lifecycle.State.STARTED)
        liveData.observe(owner) { received += it }
        liveData.setValue(1)

        owner.moveTo(Lifecycle.State.CREATED)
        owner.moveTo(Lifecycle.State.STARTED)
        assertEquals(listOf(1), received)
    }

    @Test
    fun `destroyed owner is removed`() {
        owner.moveTo(Lifecycle.State.STARTED)
        liveData.observe(owner) { received += it }
        owner.moveTo(Lifecycle.State.DESTROYED)
        liveData.setValue(1)
        assertEquals(emptyList<Int>(), received)
    }
}
