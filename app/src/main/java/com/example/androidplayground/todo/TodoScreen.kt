package com.example.androidplayground.todo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ListItem
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun TodoScreen(viewModel: TodoViewModel = koinViewModel()) {
    val todos = viewModel.todos
    var input by rememberSaveable { mutableStateOf("") }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                label = { Text("New todo") },
            )
            Button(onClick = { viewModel.add(input); input = "" }) { Text("Add") }
        }

        if (todos.isEmpty()) Text("No todos yet")

        LazyColumn {
            items(todos, key = { it.id }) { todo ->
                ListItem(
                    leadingContent = { Checkbox(todo.done, onCheckedChange = { viewModel.toggle(todo.id) }) },
                    headlineContent = {
                        Text(todo.title, textDecoration = if (todo.done) TextDecoration.LineThrough else null)
                    },
                    trailingContent = { TextButton(onClick = { viewModel.delete(todo.id) }) { Text("Delete") } },
                )
            }
        }
    }
}
