package com.example.androidplayground.images

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class ImageLoaderTest {
    @get:Rule val temp = TemporaryFolder()

    @Test
    fun `second request comes from disk`() {
        var downloads = 0
        val loader = ImageLoader(temp.root, download = { downloads++; it.toByteArray() })

        loader.getBytes("a")
        val second = loader.getBytes("a")

        assertEquals(1, downloads)
        assertArrayEquals("a".toByteArray(), second)
    }
}
