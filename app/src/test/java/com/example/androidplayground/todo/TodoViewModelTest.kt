package com.example.androidplayground.todo

import com.example.androidplayground.todo.data.Todo
import com.example.androidplayground.todo.data.TodoRepository
import com.example.androidplayground.todo.ui.TodoFilter
import com.example.androidplayground.todo.ui.TodoViewModel
import com.example.androidplayground.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

private class FakeTodoRepository : TodoRepository {
    override val todos = MutableStateFlow<List<Todo>>(emptyList())
    private var nextId = 0
    override suspend fun add(title: String) = todos.update { it + Todo("${nextId++}", title) }
    override suspend fun toggle(id: String) =
        todos.update { list -> list.map { if (it.id == id) it.copy(isDone = !it.isDone) else it } }
    override suspend fun delete(id: String) = todos.update { list -> list.filterNot { it.id == id } }
}

@OptIn(ExperimentalCoroutinesApi::class)
class TodoViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    // lazy: create the ViewModel only after the rule has replaced Dispatchers.Main
    private val viewModel by lazy { TodoViewModel(FakeTodoRepository()) }

    @Test
    fun `blank title is not added and filter shows only done`() = runTest(mainRule.dispatcher) {
        viewModel.state.launchIn(backgroundScope) // WhileSubscribed needs a collector
        viewModel.add("  ")
        viewModel.add("milk")
        viewModel.add("eggs")
        advanceUntilIdle()
        assertEquals(2, viewModel.state.value.todos.size)

        viewModel.toggle("0")
        viewModel.setFilter(TodoFilter.DONE)
        advanceUntilIdle()
        assertEquals(listOf("milk"), viewModel.state.value.todos.map { it.title })
    }
}
