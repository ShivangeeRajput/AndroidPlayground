package com.example.androidplayground.stocks.data

import com.example.androidplayground.core.safeCall
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StockRepository(private val api: StockApi, private val intervalMillis: Long = 2_000) {

    fun stocks(): Flow<Result<List<Stock>>> = poll { api.stocks() }

    fun stock(symbol: String): Flow<Result<Stock>> = poll { api.stock(symbol) }

    /**
     * Cold flow: polls only while someone collects. The next request starts after the
     * previous one finishes, so requests never pile up. A failure does not end the flow.
     */
    private fun <T> poll(request: suspend () -> T): Flow<Result<T>> = flow {
        while (true) {
            emit(safeCall { request() })
            delay(intervalMillis)
        }
    }
}
