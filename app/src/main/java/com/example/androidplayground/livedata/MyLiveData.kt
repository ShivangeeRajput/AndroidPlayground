package com.example.androidplayground.livedata

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.Executor

fun interface Observer<T> {
    fun onChanged(value: T)
}

private val mainThread: Executor by lazy {
    val handler = Handler(Looper.getMainLooper())
    Executor { handler.post(it) }
}

/**
 * Simplified LiveData:
 * 1. lifecycle aware: delivers only while the owner is at least STARTED
 * 2. sticky: a new or returning observer gets the latest value, once
 * 3. removes itself when the owner is DESTROYED
 * observe / setValue / removeObserver must be called on the main thread.
 */
open class MyLiveData<T>(private val main: Executor = mainThread) {

    private inner class Entry(val observer: Observer<T>, val owner: LifecycleOwner?) {
        var seenVersion = NO_VERSION
        var lifecycleObserver: LifecycleEventObserver? = null
        val isActive get() = owner == null || owner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
    }

    private val entries = LinkedHashMap<Observer<T>, Entry>()
    private var version = NO_VERSION
    private var data: T? = null

    /** Latest value, null if never set. */
    val value: T? get() = data

    fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED || observer in entries) return
        val entry = Entry(observer, owner)
        entries[observer] = entry
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) removeObserver(observer) else dispatch(entry)
        }
        entry.lifecycleObserver = lifecycleObserver
        owner.lifecycle.addObserver(lifecycleObserver) // replays events, so it delivers immediately
    }

    /** No lifecycle: the caller must call removeObserver, otherwise it leaks. */
    fun observeForever(observer: Observer<T>) {
        if (observer in entries) return
        val entry = Entry(observer, owner = null)
        entries[observer] = entry
        dispatch(entry)
    }

    fun removeObserver(observer: Observer<T>) {
        val entry = entries.remove(observer) ?: return
        entry.lifecycleObserver?.let { entry.owner?.lifecycle?.removeObserver(it) }
    }

    protected open fun setValue(value: T) {
        data = value
        version++
        // Copy, because an observer may add or remove observers while we loop.
        entries.values.toList().forEach(::dispatch)
    }

    /** Safe from any thread. */
    protected open fun postValue(value: T) = main.execute { setValue(value) }

    @Suppress("UNCHECKED_CAST")
    private fun dispatch(entry: Entry) {
        if (entries[entry.observer] !== entry) return // removed meanwhile
        if (!entry.isActive || entry.seenVersion >= version) return
        entry.seenVersion = version
        entry.observer.onChanged(data as T)
    }

    private companion object {
        const val NO_VERSION = -1
    }
}

/** Read only type is exposed to the UI, mutable one stays inside the ViewModel. */
class MyMutableLiveData<T>(main: Executor = mainThread) : MyLiveData<T>(main) {
    public override fun setValue(value: T) = super.setValue(value)
    public override fun postValue(value: T) = super.postValue(value)
}
