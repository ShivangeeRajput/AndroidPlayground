package com.example.androidplayground.stocks

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class StockViewModel(private val api: ApiService) : ViewModel() {
    var stocks by mutableStateOf<List<Stock>>(emptyList())
        private set
    var isError by mutableStateOf(false)
        private set

    private var job: Job? = null

    fun startPolling() {
        if (job?.isActive == true) return
        job = viewModelScope.launch {
            while (true) {
                try {
                    stocks = api.getStocks()
                    isError = false
                } catch (e: CancellationException) {
                    throw e // cancel ko kabhi mat rokna
                } catch (e: Exception) {
                    isError = true // purana data rehne do
                }
                delay(2_000)
            }
        }
    }

    fun stopPolling() {
        job?.cancel()
    }
}
