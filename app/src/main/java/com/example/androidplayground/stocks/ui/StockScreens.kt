package com.example.androidplayground.stocks.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.androidplayground.core.LoadingView
import com.example.androidplayground.stocks.data.Stock
import org.koin.androidx.compose.koinViewModel

@Composable
fun StockListScreen(onClick: (String) -> Unit, viewModel: StockListViewModel = koinViewModel()) {
    // collectAsStateWithLifecycle stops collecting in background, which stops polling.
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize()) {
        ErrorBanner(state.error)
        val stocks = state.data
        if (stocks == null) {
            LoadingView()
        } else {
            LazyColumn {
                items(stocks, key = { it.symbol }) { stock ->
                    ListItem(
                        modifier = Modifier.clickable { onClick(stock.symbol) },
                        headlineContent = { Text(stock.symbol) },
                        supportingContent = { Text(stock.name) },
                        trailingContent = { Price(stock) },
                    )
                }
            }
        }
    }
}

@Composable
fun StockDetailScreen(viewModel: StockDetailViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        ErrorBanner(state.error)
        val stock = state.data
        if (stock == null) {
            LoadingView()
        } else {
            Text(stock.name, style = MaterialTheme.typography.headlineSmall)
            Text(stock.symbol)
            Price(stock)
        }
    }
}

@Composable
private fun ErrorBanner(message: String?) {
    message?.let { Text(it, Modifier.padding(8.dp), color = MaterialTheme.colorScheme.error) }
}

@Composable
private fun Price(stock: Stock) {
    Text(
        text = "₹%.2f (%+.2f%%)".format(stock.price, stock.changePercent),
        color = if (stock.changePercent >= 0) Color(0xFF2E7D32) else Color(0xFFC62828),
    )
}
