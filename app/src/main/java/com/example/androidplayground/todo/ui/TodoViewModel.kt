package com.example.androidplayground.todo.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplayground.core.safeCall
import com.example.androidplayground.todo.data.Todo
import com.example.androidplayground.todo.data.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class TodoFilter { ALL, ACTIVE, DONE }

data class TodoUiState(
    val todos: List<Todo> = emptyList(),
    val filter: TodoFilter = TodoFilter.ALL,
    val isLoading: Boolean = true,
    val error: String? = null,
)

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    private val filter = MutableStateFlow(TodoFilter.ALL)
    private val error = MutableStateFlow<String?>(null)

    private val todos = repository.todos.catch {
        error.value = "Could not load todos"
        emit(emptyList())
    }

    // State is derived: whenever data, filter or error changes, UI state is rebuilt.
    val state = combine(todos, filter, error) { list, selected, message ->
        val visible = when (selected) {
            TodoFilter.ALL -> list
            TodoFilter.ACTIVE -> list.filterNot { it.isDone }
            TodoFilter.DONE -> list.filter { it.isDone }
        }
        TodoUiState(visible, selected, isLoading = false, error = message)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TodoUiState())

    fun add(title: String) {
        if (title.isBlank()) return
        write { repository.add(title.trim()) }
    }

    fun toggle(id: String) = write { repository.toggle(id) }

    fun delete(id: String) = write { repository.delete(id) }

    fun setFilter(value: TodoFilter) = filter.update { value }

    private fun write(block: suspend () -> Unit) {
        viewModelScope.launch {
            safeCall { block() }
                .onSuccess { error.value = null }
                .onFailure {
                    Log.e("Todo", "write failed", it)
                    error.value = "Could not save. Try again."
                }
        }
    }
}
