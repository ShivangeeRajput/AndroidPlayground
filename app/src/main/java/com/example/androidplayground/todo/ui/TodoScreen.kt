package com.example.androidplayground.todo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidplayground.core.EmptyView
import com.example.androidplayground.core.LoadingView
import com.example.androidplayground.todo.data.Todo
import org.koin.androidx.compose.koinViewModel

@Composable
fun TodoScreen(viewModel: TodoViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    TodoContent(state, viewModel::add, viewModel::toggle, viewModel::delete, viewModel::setFilter)
}

/** Stateless: easy to preview and test. */
@Composable
private fun TodoContent(
    state: TodoUiState,
    onAdd: (String) -> Unit,
    onToggle: (String) -> Unit,
    onDelete: (String) -> Unit,
    onFilter: (TodoFilter) -> Unit,
) {
    var input by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                label = { Text("New todo") },
                singleLine = true,
            )
            Button(
                onClick = { onAdd(input); input = "" },
                enabled = input.isNotBlank(),
                modifier = Modifier.padding(start = 8.dp),
            ) { Text("Add") }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TodoFilter.entries.forEach {
                FilterChip(selected = state.filter == it, onClick = { onFilter(it) }, label = { Text(it.name) })
            }
        }
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }

        when {
            state.isLoading -> LoadingView()
            state.todos.isEmpty() -> EmptyView("Nothing here")
            else -> LazyColumn(Modifier.fillMaxWidth()) {
                items(state.todos, key = { it.id }) { TodoRow(it, onToggle, onDelete) }
            }
        }
    }
}

@Composable
private fun TodoRow(todo: Todo, onToggle: (String) -> Unit, onDelete: (String) -> Unit) {
    ListItem(
        leadingContent = { Checkbox(checked = todo.isDone, onCheckedChange = { onToggle(todo.id) }) },
        headlineContent = {
            Text(todo.title, textDecoration = if (todo.isDone) TextDecoration.LineThrough else null)
        },
        trailingContent = { TextButton(onClick = { onDelete(todo.id) }) { Text("Delete") } },
    )
}
