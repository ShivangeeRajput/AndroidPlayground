package com.example.androidplayground.todo

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TodoViewModel(private val store: TodoStore) : ViewModel() {
    private val _todos = MutableStateFlow(store.load())
    val todos = _todos.asStateFlow()

    fun add(title: String) {
        if (title.isBlank()) return
        val id = (_todos.value.maxOfOrNull { it.id } ?: 0) + 1
        update(_todos.value + Todo(id, title.trim()))
    }

    fun toggle(id: Long) = update(_todos.value.map { if (it.id == id) it.copy(done = !it.done) else it })

    fun delete(id: Long) = update(_todos.value.filter { it.id != id })

    // Har change: state badlo aur save karo
    private fun update(newList: List<Todo>) {
        _todos.value = newList
        store.save(newList)
    }
}
