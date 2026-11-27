package com.example.androidplayground.todo

import org.junit.Assert.assertEquals
import org.junit.Test

private class FakeStore : TodoStore {
    var saved = emptyList<Todo>()
    override fun load() = saved
    override fun save(todos: List<Todo>) {
        saved = todos
    }
}

class TodoViewModelTest {
    private val store = FakeStore()
    private val viewModel = TodoViewModel(store)

    @Test
    fun `add, toggle and delete are saved`() {
        viewModel.add("  ") // blank ignore
        viewModel.add("milk")
        viewModel.add("eggs")
        viewModel.toggle(1)
        viewModel.delete(2)

        assertEquals(listOf(Todo(1, "milk", done = true)), store.saved)
    }
}
