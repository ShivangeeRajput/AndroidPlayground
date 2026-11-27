package com.example.androidplayground.stocks

import com.example.androidplayground.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

private class FakeApi : ApiService {
    var calls = 0
    override suspend fun getStocks() = listOf(Stock("TCS", ++calls))
}

@OptIn(ExperimentalCoroutinesApi::class)
class StockViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    @Test
    fun `polls every 2 seconds until stopped`() = runTest(mainRule.dispatcher) {
        val api = FakeApi()
        val viewModel = StockViewModel(api)

        viewModel.startPolling()
        runCurrent()
        advanceTimeBy(2_001)
        assertEquals(2, api.calls)

        viewModel.stopPolling()
        advanceTimeBy(10_000)
        assertEquals(2, api.calls)
    }
}
