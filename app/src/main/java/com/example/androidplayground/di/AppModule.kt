package com.example.androidplayground.di

import com.example.androidplayground.core.createOkHttpClient
import com.example.androidplayground.stocks.data.FakeStockApi
import com.example.androidplayground.stocks.data.StockApi
import com.example.androidplayground.stocks.data.StockRepository
import com.example.androidplayground.stocks.ui.StockDetailViewModel
import com.example.androidplayground.stocks.ui.StockListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// Register your dependencies here, e.g.
//   single { createRetrofit("https://dummyjson.com/", get()).create(MyApi::class.java) }
//   singleOf(::MyRepositoryImpl) { bind<MyRepository>() }
//   viewModelOf(::MyViewModel)
val appModule = module {
    single { createOkHttpClient() }
    single<StockApi> { FakeStockApi() }
    single { StockRepository(get()) }
    viewModelOf(::StockListViewModel)
    viewModelOf(::StockDetailViewModel)
}
