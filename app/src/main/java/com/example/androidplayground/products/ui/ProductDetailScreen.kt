package com.example.androidplayground.products.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidplayground.core.ErrorView
import com.example.androidplayground.core.LoadingView
import com.example.androidplayground.core.UiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ProductDetailScreen(viewModel: ProductDetailViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val s = state) {
        UiState.Loading -> LoadingView()
        is UiState.Error -> ErrorView(s.message, viewModel::load)
        is UiState.Success -> Column(Modifier.fillMaxSize().padding(16.dp)) {
            Text(s.data.title, style = MaterialTheme.typography.headlineSmall)
            Text("$${s.data.price}", style = MaterialTheme.typography.titleMedium)
            Text(s.data.description, Modifier.padding(top = 8.dp))
        }
    }
}
