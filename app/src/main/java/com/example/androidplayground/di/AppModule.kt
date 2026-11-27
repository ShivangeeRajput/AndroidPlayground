package com.example.androidplayground.di

import com.example.androidplayground.timer.Timer
import com.example.androidplayground.timer.TimerViewModel
import kotlinx.coroutines.MainScope
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { Timer(MainScope()) } // service wala: screen se zyada jeeta hai
    viewModelOf(::TimerViewModel)
}
