package com.example.androidplayground.di

import com.example.androidplayground.core.createOkHttpClient
import com.example.androidplayground.core.createRetrofit
import com.example.androidplayground.products.data.ProductApi
import com.example.androidplayground.products.data.ProductRepository
import com.example.androidplayground.products.data.ProductRepositoryImpl
import com.example.androidplayground.products.ui.ProductDetailViewModel
import com.example.androidplayground.products.ui.ProductListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// Register your dependencies here, e.g.
//   single { createRetrofit("https://dummyjson.com/", get()).create(MyApi::class.java) }
//   singleOf(::MyRepositoryImpl) { bind<MyRepository>() }
//   viewModelOf(::MyViewModel)
val appModule = module {
    single { createOkHttpClient() }
    single { createRetrofit("https://dummyjson.com/", get()).create(ProductApi::class.java) }
    single<ProductRepository> { ProductRepositoryImpl(get()) }
    viewModelOf(::ProductListViewModel)
    viewModelOf(::ProductDetailViewModel)
}
