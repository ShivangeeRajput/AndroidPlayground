package com.example.androidplayground.stocks

import com.example.androidplayground.stocks.data.Stock
import com.example.androidplayground.stocks.data.StockApi
import com.example.androidplayground.stocks.data.StockRepository
import com.example.androidplayground.stocks.ui.StockListViewModel
import com.example.androidplayground.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test
import java.io.IOException

/** First call succeeds, every later call fails. */
private class FlakyStockApi : StockApi {
    private var calls = 0
    private val tcs = Stock("TCS", "Tata", 1.0, 0.0)

    override suspend fun stocks(): List<Stock> =
        if (calls++ == 0) listOf(tcs) else throw IOException()

    override suspend fun stock(symbol: String) = tcs
}

@OptIn(ExperimentalCoroutinesApi::class)
class StockListViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    private val viewModel by lazy { StockListViewModel(StockRepository(FlakyStockApi(), 1_000)) }

    @Test
    fun `keeps old data when a later poll fails`() = runTest(mainRule.dispatcher) {
        viewModel.state.launchIn(backgroundScope)
        runCurrent()
        assertEquals(1, viewModel.state.value.data?.size)

        advanceTimeBy(1_001)
        assertEquals(1, viewModel.state.value.data?.size)
        assertNotNull(viewModel.state.value.error)
    }
}
