package com.example.androidplayground.images

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.security.MessageDigest

/** Size is counted in KB. Uses 1/8 of the app's max heap. */
class BitmapMemoryCache(
    maxKb: Int = (Runtime.getRuntime().maxMemory() / 1024 / 8).toInt(),
) : MemoryCache<Bitmap> {
    private val cache = object : LruCache<String, Bitmap>(maxKb) {
        override fun sizeOf(key: String, value: Bitmap) = value.byteCount / 1024
    }

    override fun get(key: String): Bitmap? = cache.get(key)
    override fun put(key: String, value: Bitmap) {
        cache.put(key, value)
    }
}

/** One file per URL. No size limit here: mention eviction as a follow up. */
class FileDiskCache(private val directory: File) : DiskCache {
    override fun get(key: String): ByteArray? = file(key).takeIf { it.exists() }?.readBytes()

    override fun put(key: String, bytes: ByteArray) {
        directory.mkdirs()
        // Write to a temp file and rename, so a half written file is never read.
        val temp = File.createTempFile("img", ".tmp", directory)
        temp.writeBytes(bytes)
        if (!temp.renameTo(file(key))) temp.delete()
    }

    private fun file(key: String): File {
        val hash = MessageDigest.getInstance("SHA-256").digest(key.toByteArray())
        return File(directory, hash.joinToString("") { "%02x".format(it) })
    }
}

fun downloadBytes(client: OkHttpClient, url: String): ByteArray =
    client.newCall(Request.Builder().url(url).build()).execute().use { response ->
        if (!response.isSuccessful) throw IOException("HTTP ${response.code} for $url")
        checkNotNull(response.body).bytes()
    }

/** Read only the size first, then decode a smaller bitmap to save memory. */
fun decodeSampled(bytes: ByteArray, maxSize: Int = 512): Bitmap? {
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)

    var sample = 1
    while (maxOf(options.outWidth, options.outHeight) / sample > maxSize) sample *= 2

    options.inJustDecodeBounds = false
    options.inSampleSize = sample
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size, options)
}
