package com.example.androidplayground.todo

import android.content.Context
import com.google.gson.Gson

data class Todo(val id: Long, val title: String, val done: Boolean = false)

// Interface isliye taaki test mein fake store de sakein
interface TodoStore {
    fun load(): List<Todo>
    fun save(todos: List<Todo>)
}

// Poori list ek JSON string ban ke SharedPreferences mein
class PrefsTodoStore(context: Context) : TodoStore {
    private val prefs = context.getSharedPreferences("todos", Context.MODE_PRIVATE)
    private val gson = Gson()

    override fun load(): List<Todo> {
        val json = prefs.getString("list", null) ?: return emptyList()
        return gson.fromJson(json, Array<Todo>::class.java).toList()
    }

    override fun save(todos: List<Todo>) {
        prefs.edit().putString("list", gson.toJson(todos)).apply()
    }
}
