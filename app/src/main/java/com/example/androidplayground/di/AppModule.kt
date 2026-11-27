package com.example.androidplayground.di

import android.os.SystemClock
import com.example.androidplayground.core.createOkHttpClient
import com.example.androidplayground.timer.TimerEngine
import com.example.androidplayground.timer.ui.TimerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// Register your dependencies here, e.g.
//   single { createRetrofit("https://dummyjson.com/", get()).create(MyApi::class.java) }
//   singleOf(::MyRepositoryImpl) { bind<MyRepository>() }
//   viewModelOf(::MyViewModel)
val appModule = module {
    single { createOkHttpClient() }
    // App-wide engine used by the service version: it must outlive any screen.
    single { TimerEngine(CoroutineScope(SupervisorJob() + Dispatchers.Main), SystemClock::elapsedRealtime) }
    viewModelOf(::TimerViewModel)
}
