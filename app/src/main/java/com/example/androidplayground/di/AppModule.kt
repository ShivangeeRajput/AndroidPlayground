package com.example.androidplayground.di

import com.example.androidplayground.core.createOkHttpClient
import com.example.androidplayground.todo.data.DataStoreTodoRepository
import com.example.androidplayground.todo.data.TodoRepository
import com.example.androidplayground.todo.ui.TodoViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

// Register your dependencies here, e.g.
//   single { createRetrofit("https://dummyjson.com/", get()).create(MyApi::class.java) }
//   singleOf(::MyRepositoryImpl) { bind<MyRepository>() }
//   viewModelOf(::MyViewModel)
val appModule = module {
    single { createOkHttpClient() }
    single<TodoRepository> { DataStoreTodoRepository(get()) }
    viewModelOf(::TodoViewModel)
}
