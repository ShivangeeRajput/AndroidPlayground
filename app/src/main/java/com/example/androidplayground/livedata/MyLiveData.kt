package com.example.androidplayground.livedata

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

class MyLiveData<T : Any> {
    private class Entry(val owner: LifecycleOwner) {
        var lastVersion = 0
        lateinit var lifecycleObserver: LifecycleEventObserver
    }

    private val observers = mutableMapOf<(T) -> Unit, Entry>()
    private var version = 0 // har setValue pe +1

    var value: T? = null
        private set

    fun observe(owner: LifecycleOwner, observer: (T) -> Unit) {
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED || observer in observers) return
        val entry = Entry(owner)
        entry.lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) removeObserver(observer) else send(observer)
        }
        observers[observer] = entry
        owner.lifecycle.addObserver(entry.lifecycleObserver)
    }

    fun removeObserver(observer: (T) -> Unit) {
        val entry = observers.remove(observer) ?: return
        entry.owner.lifecycle.removeObserver(entry.lifecycleObserver)
    }

    // Main thread se
    fun setValue(newValue: T) {
        value = newValue
        version++
        observers.keys.toList().forEach { send(it) }
    }

    // Kisi bhi thread se
    fun postValue(newValue: T) {
        Handler(Looper.getMainLooper()).post { setValue(newValue) }
    }

    // Sirf tab bhejo jab owner STARTED ho aur usne ye value na dekhi ho
    private fun send(observer: (T) -> Unit) {
        val entry = observers[observer] ?: return
        val data = value ?: return
        if (!entry.owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) return
        if (entry.lastVersion == version) return
        entry.lastVersion = version
        observer(data)
    }
}
