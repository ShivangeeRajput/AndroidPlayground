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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidplayground.core.EmptyView
import com.example.androidplayground.core.ErrorView
import com.example.androidplayground.core.LoadingView
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductListScreen(onClick: (Int) -> Unit, viewModel: ProductViewModel = koinViewModel()) {
    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = viewModel.query,
            onValueChange = { viewModel.search(it) },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            label = { Text("Search") },
        )

        LazyColumn(Modifier.weight(1f)) {
            itemsIndexed(viewModel.products) { index, product ->
                ListItem(
                    modifier = Modifier.clickable { onClick(product.id) },
                    headlineContent = { Text(product.title) },
                    supportingContent = { Text("$${product.price}") },
                )
                // Last item dikha = agla page mangao
                if (index == viewModel.products.lastIndex) {
                    LaunchedEffect(viewModel.products.size) { viewModel.loadMore() }
                }
            }
        }

        // List ke neeche: loading, error ya empty
        when {
            viewModel.isLoading -> LoadingView(Modifier.fillMaxWidth().padding(16.dp))
            viewModel.isError -> ErrorView("Something went wrong", { viewModel.load() }, Modifier.fillMaxWidth())
            viewModel.products.isEmpty() -> EmptyView("No results", Modifier.fillMaxWidth())
        }
    }
}
