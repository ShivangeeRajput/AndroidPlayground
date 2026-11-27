package com.example.androidplayground.di

import com.example.androidplayground.stocks.ApiService
import com.example.androidplayground.stocks.FakeApiService
import com.example.androidplayground.stocks.StockViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<ApiService> { FakeApiService() }
    viewModelOf(::StockViewModel)
}
