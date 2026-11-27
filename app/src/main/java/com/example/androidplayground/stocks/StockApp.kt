package com.example.androidplayground.stocks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.compose.koinViewModel

// Ek hi ViewModel dono screens ke liye, isliye polling bhi ek hi
@Composable
fun StockApp(modifier: Modifier = Modifier, viewModel: StockViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navController = rememberNavController()

    // App saamne aaye to start, background mein jaye to stop
    LifecycleStartEffect(viewModel) {
        viewModel.startPolling()
        onStopOrDispose { viewModel.stopPolling() }
    }

    NavHost(navController, startDestination = "list", modifier = modifier) {
        composable("list") {
            StockListScreen(state, onClick = { navController.navigate("detail/$it") })
        }
        composable("detail/{symbol}") { entry ->
            val symbol = entry.arguments?.getString("symbol")
            StockDetailScreen(state.stocks.find { it.symbol == symbol })
        }
    }
}
