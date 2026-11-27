package com.example.androidplayground.di

import com.example.androidplayground.todo.PrefsTodoStore
import com.example.androidplayground.todo.TodoStore
import com.example.androidplayground.todo.TodoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single<TodoStore> { PrefsTodoStore(androidContext()) }
    viewModelOf(::TodoViewModel)
}
