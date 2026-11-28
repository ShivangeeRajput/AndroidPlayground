package com.example.androidplayground.products

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.androidplayground.core.ErrorView
import com.example.androidplayground.core.LoadingView
import org.koin.androidx.compose.koinViewModel

@Composable
fun DetailScreen(id: Int, viewModel: DetailViewModel = koinViewModel()) {
    LaunchedEffect(id) { viewModel.load(id) }

    val product = viewModel.product
    when {
        product != null -> Column(Modifier.padding(16.dp)) {
            Text(product.title, style = MaterialTheme.typography.headlineSmall)
            Text("$${product.price}")
            Text(product.description)
        }
        viewModel.isError -> ErrorView("Could not load product", { viewModel.load(id) })
        else -> LoadingView()
    }
}
