package com.example.androidplayground.di

import com.example.androidplayground.payment.PaymentProcessor
import com.example.androidplayground.payment.PaymentViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::PaymentProcessor)
    viewModelOf(::PaymentViewModel)
}
