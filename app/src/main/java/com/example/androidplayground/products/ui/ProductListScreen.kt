package com.example.androidplayground.products.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidplayground.core.EmptyView
import com.example.androidplayground.core.ErrorView
import com.example.androidplayground.core.LoadingView
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductListScreen(onClick: (Int) -> Unit, viewModel: ProductListViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = state.query,
            onValueChange = viewModel::onQueryChange,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            label = { Text("Search") },
            singleLine = true,
        )
        when {
            state.items.isNotEmpty() -> ProductList(state, onClick, viewModel::loadMore, viewModel::retry)
            state.isLoading -> LoadingView()
            state.error != null -> ErrorView(state.error!!, viewModel::retry)
            else -> EmptyView("No results")
        }
    }
}

@Composable
private fun ProductList(
    state: ProductListState,
    onClick: (Int) -> Unit,
    onLoadMore: () -> Unit,
    onRetry: () -> Unit,
) {
    val listState = rememberLazyListState()
    // derivedStateOf: recompose only when this boolean flips, not on every scrolled pixel.
    val nearEnd by remember {
        derivedStateOf {
            val last = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            last >= listState.layoutInfo.totalItemsCount - 5
        }
    }
    LaunchedEffect(nearEnd, state.items.size) { if (nearEnd) onLoadMore() }

    LazyColumn(state = listState) {
        items(state.items, key = { it.id }) { product ->
            ListItem(
                modifier = Modifier.clickable { onClick(product.id) },
                headlineContent = { Text(product.title) },
                supportingContent = { Text("$${product.price}") },
            )
            HorizontalDivider()
        }
        // Footer for the next page: spinner or retry
        if (state.isLoading) item { LoadingView(Modifier.fillMaxWidth().padding(16.dp)) }
        state.error?.let { item { ErrorView(it, onRetry, Modifier.fillMaxWidth()) } }
    }
}
