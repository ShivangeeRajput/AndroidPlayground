package com.example.androidplayground.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

// Order: memory -> disk -> internet
class ImageLoader(
    private val folder: File,
    private val download: (String) -> ByteArray = { URL(it).readBytes() },
) {
    // Memory cache: last 50 images. lazy isliye ki unit test mein LruCache nahi hota
    private val memory by lazy { LruCache<String, Bitmap>(50) }

    suspend fun load(url: String): Bitmap? {
        memory.get(url)?.let { return it }

        return withContext(Dispatchers.IO) {
            val bytes = getBytes(url)
            val options = BitmapFactory.Options().apply { inSampleSize = 2 } // aadha size, kam memory
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
                ?.also { memory.put(url, it) }
        }
    }

    // Disk cache: file mein hai to wahi, warna download karke save
    fun getBytes(url: String): ByteArray {
        val file = File(folder, url.hashCode().toString())
        if (file.exists()) return file.readBytes()

        val bytes = download(url)
        folder.mkdirs()
        val temp = File.createTempFile("img", null, folder) // adhi likhi file kabhi na padhi jaye
        temp.writeBytes(bytes)
        temp.renameTo(file)
        return bytes
    }
}
