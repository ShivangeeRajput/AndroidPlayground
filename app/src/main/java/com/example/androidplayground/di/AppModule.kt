package com.example.androidplayground.di

import com.example.androidplayground.products.DetailViewModel
import com.example.androidplayground.products.ProductRepository
import com.example.androidplayground.products.ProductViewModel
import com.example.androidplayground.products.RetrofitClient
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// DI: kaunsa object kaise banega, ye Koin ko yahan batate hain
val appModule = module {
    single { ProductRepository(RetrofitClient.api) }
    viewModelOf(::ProductViewModel)
    viewModelOf(::DetailViewModel)
}
