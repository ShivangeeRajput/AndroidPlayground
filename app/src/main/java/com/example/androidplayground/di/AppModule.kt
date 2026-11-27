package com.example.androidplayground.di

import com.example.androidplayground.livedata.CounterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::CounterViewModel)
}
