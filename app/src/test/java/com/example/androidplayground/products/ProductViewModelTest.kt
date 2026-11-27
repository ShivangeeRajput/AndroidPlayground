package com.example.androidplayground.products

import com.example.androidplayground.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

// Fake = internet ke bina nakli API
private class FakeApi : ApiService {
    val queries = mutableListOf<String>()
    private val all = List(30) { Product(it, "item $it", "", 1.0) }

    override suspend fun getProducts(query: String, skip: Int, limit: Int): ProductResponse {
        queries.add(query)
        return ProductResponse(all.drop(skip).take(limit), all.size)
    }

    override suspend fun getProduct(id: Int) = all[id]
}

private class FakeStore : SearchStore {
    override var lastQuery = ""
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    private val api = FakeApi()
    private val store = FakeStore()

    @Test
    fun `loadMore adds the next page`() = runTest(mainRule.dispatcher) {
        val viewModel = ProductViewModel(ProductRepository(api, store))
        advanceUntilIdle()
        assertEquals(20, viewModel.state.value.products.size)

        viewModel.loadMore()
        advanceUntilIdle()
        assertEquals(30, viewModel.state.value.products.size)
    }

    @Test
    fun `fast typing calls the api only for the last text and saves it`() = runTest(mainRule.dispatcher) {
        val viewModel = ProductViewModel(ProductRepository(api, store))
        advanceUntilIdle()

        viewModel.search("p")
        viewModel.search("ph")
        advanceUntilIdle()

        assertEquals(listOf("", "ph"), api.queries)
        assertEquals("ph", store.lastQuery)
    }
}
