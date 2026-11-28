package com.example.androidplayground.todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class TodoViewModel(private val store: TodoStore) : ViewModel() {
    var todos by mutableStateOf(store.load())
        private set

    fun add(title: String) {
        if (title.isBlank()) return
        val id = (todos.maxOfOrNull { it.id } ?: 0) + 1
        update(todos + Todo(id, title.trim()))
    }

    fun toggle(id: Long) = update(todos.map { if (it.id == id) it.copy(done = !it.done) else it })

    fun delete(id: Long) = update(todos.filter { it.id != id })

    // Har change: state badlo aur save karo
    private fun update(newList: List<Todo>) {
        todos = newList
        store.save(newList)
    }
}
