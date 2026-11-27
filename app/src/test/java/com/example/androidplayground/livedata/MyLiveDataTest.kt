package com.example.androidplayground.livedata

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import org.junit.Assert.assertEquals
import org.junit.Test

private class TestOwner : LifecycleOwner {
    val registry = LifecycleRegistry.createUnsafe(this) // test mein main thread nahi hota
    override val lifecycle: Lifecycle get() = registry
}

class MyLiveDataTest {

    @Test
    fun `delivers only when started and stops after destroy`() {
        val liveData = MyLiveData<Int>()
        val owner = TestOwner()
        val received = mutableListOf<Int>()

        owner.registry.currentState = Lifecycle.State.CREATED
        liveData.observe(owner) { received.add(it) }
        liveData.setValue(1)
        liveData.setValue(2)
        assertEquals(emptyList<Int>(), received)

        owner.registry.currentState = Lifecycle.State.STARTED
        owner.registry.currentState = Lifecycle.State.RESUMED
        assertEquals(listOf(2), received) // sirf latest, ek baar

        owner.registry.currentState = Lifecycle.State.DESTROYED
        liveData.setValue(3)
        assertEquals(listOf(2), received)
    }
}
