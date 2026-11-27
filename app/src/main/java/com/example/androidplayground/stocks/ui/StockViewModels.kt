package com.example.androidplayground.stocks.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplayground.stocks.data.Stock
import com.example.androidplayground.stocks.data.StockRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn

/** data and error together: on a failed poll we keep showing the old prices. */
data class PollState<T>(val data: T? = null, val error: String? = null)

private fun <T> Flow<Result<T>>.toPollState(viewModel: ViewModel): StateFlow<PollState<T>> =
    runningFold(PollState<T>()) { previous, result ->
        result.fold(
            onSuccess = { PollState(data = it) },
            onFailure = { previous.copy(error = "Could not refresh, retrying…") },
        )
    }.stateIn(
        viewModel.viewModelScope,
        // Stops polling 5s after the UI stops collecting (background / other screen).
        SharingStarted.WhileSubscribed(5_000),
        PollState(),
    )

class StockListViewModel(repository: StockRepository) : ViewModel() {
    val state = repository.stocks().toPollState(this)
}

class StockDetailViewModel(savedStateHandle: SavedStateHandle, repository: StockRepository) : ViewModel() {
    private val symbol: String = checkNotNull(savedStateHandle["symbol"])
    val state = repository.stock(symbol).toPollState(this)
}
