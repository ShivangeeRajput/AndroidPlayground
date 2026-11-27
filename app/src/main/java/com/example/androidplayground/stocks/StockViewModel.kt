package com.example.androidplayground.stocks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StockState(val stocks: List<Stock> = emptyList(), val error: String? = null)

class StockViewModel(private val api: ApiService) : ViewModel() {
    private val _state = MutableStateFlow(StockState())
    val state = _state.asStateFlow()

    private var job: Job? = null

    fun startPolling() {
        if (job?.isActive == true) return
        job = viewModelScope.launch {
            while (true) {
                try {
                    _state.value = StockState(stocks = api.getStocks())
                } catch (e: CancellationException) {
                    throw e // cancel ko kabhi mat rokna
                } catch (e: Exception) {
                    _state.update { it.copy(error = "Could not refresh") } // purana data rehne do
                }
                delay(2_000)
            }
        }
    }

    fun stopPolling() {
        job?.cancel()
    }
}
