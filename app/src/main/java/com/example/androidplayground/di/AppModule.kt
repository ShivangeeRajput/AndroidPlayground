package com.example.androidplayground.di

import com.example.androidplayground.images.ImageLoader
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single { ImageLoader(File(androidContext().cacheDir, "images")) }
}
