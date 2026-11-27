package com.example.androidplayground.stocks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StockListScreen(state: StockState, onClick: (String) -> Unit) {
    Column {
        state.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (state.stocks.isEmpty()) CircularProgressIndicator()

        LazyColumn {
            items(state.stocks, key = { it.symbol }) { stock ->
                ListItem(
                    modifier = Modifier.clickable { onClick(stock.symbol) },
                    headlineContent = { Text(stock.symbol) },
                    trailingContent = { Text("₹${stock.price}") },
                )
            }
        }
    }
}

@Composable
fun StockDetailScreen(stock: Stock?) {
    Column(Modifier.padding(16.dp)) {
        if (stock == null) {
            CircularProgressIndicator()
        } else {
            Text(stock.symbol, style = MaterialTheme.typography.headlineMedium)
            Text("Price: ₹${stock.price}")
        }
    }
}
