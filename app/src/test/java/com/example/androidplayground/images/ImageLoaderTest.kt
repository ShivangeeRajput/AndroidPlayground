package com.example.androidplayground.images

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

private class MapMemoryCache : MemoryCache<String> {
    val map = mutableMapOf<String, String>()
    override fun get(key: String) = map[key]
    override fun put(key: String, value: String) {
        map[key] = value
    }
}

private class MapDiskCache : DiskCache {
    private val map = mutableMapOf<String, ByteArray>()
    override fun get(key: String) = map[key]
    override fun put(key: String, bytes: ByteArray) {
        map[key] = bytes
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class ImageLoaderTest {
    private var downloads = 0

    private fun TestScope.loader(fail: Boolean = false) = ImageLoader(
        memory = MapMemoryCache(),
        disk = MapDiskCache(),
        download = {
            downloads++
            delay(100)
            if (fail) throw IOException()
            it.toByteArray()
        },
        decode = { String(it) },
        dispatcher = StandardTestDispatcher(testScheduler),
    )

    @Test
    fun `parallel requests for one url download once`() = runTest {
        val loader = loader()
        val results = List(5) { async { loader.load("a") } }.awaitAll()
        loader.load("a") // now from memory

        assertEquals(1, downloads)
        assertTrue(results.all { it.getOrNull() == "a" })
    }

    @Test
    fun `failure is returned and can be retried`() = runTest {
        val loader = loader(fail = true)
        assertTrue(loader.load("a").isFailure)
        assertTrue(loader.load("a").isFailure)
        assertEquals(2, downloads)
    }
}
