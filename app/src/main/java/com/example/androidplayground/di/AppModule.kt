package com.example.androidplayground.di

import com.example.androidplayground.core.createOkHttpClient
import org.koin.dsl.module

// Register your dependencies here, e.g.
//   single { createRetrofit("https://dummyjson.com/", get()).create(MyApi::class.java) }
//   singleOf(::MyRepositoryImpl) { bind<MyRepository>() }
//   viewModelOf(::MyViewModel)
val appModule = module {
    single { createOkHttpClient() }
}
