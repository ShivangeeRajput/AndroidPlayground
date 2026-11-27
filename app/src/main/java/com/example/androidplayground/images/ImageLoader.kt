package com.example.androidplayground.images

import com.example.androidplayground.core.safeCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface MemoryCache<T> {
    fun get(key: String): T?
    fun put(key: String, value: T)
}

interface DiskCache {
    fun get(key: String): ByteArray?
    fun put(key: String, bytes: ByteArray)
}

/**
 * memory -> disk -> network. Generic over the image type so it is testable without Bitmap.
 */
class ImageLoader<T : Any>(
    private val memory: MemoryCache<T>,
    private val disk: DiskCache,
    private val download: suspend (url: String) -> ByteArray,
    private val decode: (ByteArray) -> T?,
    dispatcher: CoroutineDispatcher,
) {
    // Own scope: one caller scrolling away must not cancel a download others are waiting for.
    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val mutex = Mutex()
    private val inFlight = mutableMapOf<String, Deferred<Result<T>>>()

    suspend fun load(url: String): Result<T> {
        memory.get(url)?.let { return Result.success(it) }

        val request = mutex.withLock {
            inFlight.getOrPut(url) {
                scope.async {
                    val result = safeCall { fetch(url) }
                    mutex.withLock { inFlight.remove(url) }
                    result
                }
            }
        }
        return request.await()
    }

    private suspend fun fetch(url: String): T {
        val cached = disk.get(url)
        val bytes = cached ?: download(url)
        val image = checkNotNull(decode(bytes)) { "Could not decode $url" }
        if (cached == null) disk.put(url, bytes) // only save bytes that decoded fine
        memory.put(url, image)
        return image
    }
}
