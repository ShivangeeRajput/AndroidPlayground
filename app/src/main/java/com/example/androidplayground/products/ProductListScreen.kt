package com.example.androidplayground.products

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidplayground.core.EmptyView
import com.example.androidplayground.core.ErrorView
import com.example.androidplayground.core.LoadingView
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductListScreen(onClick: (Int) -> Unit, viewModel: ProductViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.query,
            onValueChange = { viewModel.search(it) },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            label = { Text("Search") },
        )

        LazyColumn(Modifier.weight(1f)) {
            itemsIndexed(state.products) { index, product ->
                ListItem(
                    modifier = Modifier.clickable { onClick(product.id) },
                    headlineContent = { Text(product.title) },
                    supportingContent = { Text("$${product.price}") },
                )
                // Last item dikha = agla page mangao
                if (index == state.products.lastIndex) {
                    LaunchedEffect(state.products.size) { viewModel.loadMore() }
                }
            }
        }

        // List ke neeche: loading, error ya empty
        val error = state.error
        when {
            state.isLoading -> LoadingView(Modifier.fillMaxWidth().padding(16.dp))
            error != null -> ErrorView(error, { viewModel.load() }, Modifier.fillMaxWidth())
            state.products.isEmpty() -> EmptyView("No results", Modifier.fillMaxWidth())
        }
    }
}
