package com.example.androidplayground.di

import com.example.androidplayground.core.createOkHttpClient
import com.example.androidplayground.payment.data.PaymentProcessor
import com.example.androidplayground.payment.ui.PaymentViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// Register your dependencies here, e.g.
//   single { createRetrofit("https://dummyjson.com/", get()).create(MyApi::class.java) }
//   singleOf(::MyRepositoryImpl) { bind<MyRepository>() }
//   viewModelOf(::MyViewModel)
val appModule = module {
    single { createOkHttpClient() }
    singleOf(::PaymentProcessor)
    viewModelOf(::PaymentViewModel)
}
