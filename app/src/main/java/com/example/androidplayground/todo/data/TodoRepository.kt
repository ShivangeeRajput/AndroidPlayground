package com.example.androidplayground.todo.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

interface TodoRepository {
    val todos: Flow<List<Todo>>
    suspend fun add(title: String)
    suspend fun toggle(id: String)
    suspend fun delete(id: String)
}

private val Context.todoDataStore by preferencesDataStore("todos")

/** Whole list stored as one JSON string. Fine for a small list; use Room if it grows. */
class DataStoreTodoRepository(private val context: Context) : TodoRepository {
    private val key = stringPreferencesKey("todo_list")
    private val gson = Gson()
    private val type = object : TypeToken<List<Todo>>() {}.type

    override val todos: Flow<List<Todo>> = context.todoDataStore.data.map { decode(it[key]) }

    override suspend fun add(title: String) = update { it + Todo(UUID.randomUUID().toString(), title) }

    override suspend fun toggle(id: String) = update { list ->
        list.map { if (it.id == id) it.copy(isDone = !it.isDone) else it }
    }

    override suspend fun delete(id: String) = update { list -> list.filterNot { it.id == id } }

    /** edit {} is atomic, so two quick updates cannot overwrite each other. */
    private suspend fun update(transform: (List<Todo>) -> List<Todo>) {
        context.todoDataStore.edit { it[key] = gson.toJson(transform(decode(it[key]))) }
    }

    private fun decode(json: String?): List<Todo> =
        if (json == null) emptyList() else gson.fromJson(json, type)
}
