package com.example.androidplayground.di

import android.graphics.Bitmap
import com.example.androidplayground.core.createOkHttpClient
import com.example.androidplayground.images.BitmapMemoryCache
import com.example.androidplayground.images.FileDiskCache
import com.example.androidplayground.images.ImageLoader
import com.example.androidplayground.images.decodeSampled
import com.example.androidplayground.images.downloadBytes
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import java.io.File
import org.koin.dsl.module

// Register your dependencies here, e.g.
//   single { createRetrofit("https://dummyjson.com/", get()).create(MyApi::class.java) }
//   singleOf(::MyRepositoryImpl) { bind<MyRepository>() }
//   viewModelOf(::MyViewModel)
val appModule = module {
    single { createOkHttpClient() }
    single<ImageLoader<Bitmap>> {
        val client: OkHttpClient = get()
        ImageLoader(
            memory = BitmapMemoryCache(),
            disk = FileDiskCache(File(androidContext().cacheDir, "images")),
            download = { url -> downloadBytes(client, url) },
            decode = { decodeSampled(it) },
            dispatcher = Dispatchers.IO,
        )
    }
}
