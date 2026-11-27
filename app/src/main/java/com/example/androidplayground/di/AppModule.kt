package com.example.androidplayground.di

import org.koin.dsl.module

// Har object yahan register karo, e.g.
//   single { MyRepository(RetrofitClient.api) }
//   viewModelOf(::MyViewModel)      // import org.koin.core.module.dsl.viewModelOf
val appModule = module {
}
