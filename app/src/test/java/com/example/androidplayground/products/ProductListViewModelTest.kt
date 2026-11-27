package com.example.androidplayground.products

import com.example.androidplayground.products.data.Product
import com.example.androidplayground.products.data.ProductPage
import com.example.androidplayground.products.data.ProductRepository
import com.example.androidplayground.products.ui.ProductListViewModel
import com.example.androidplayground.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

private class FakeProductRepository : ProductRepository {
    val queries = mutableListOf<String>()
    private val all = List(30) { Product(it, "item $it", "", 1.0) }

    override suspend fun search(query: String, skip: Int, limit: Int): Result<ProductPage> {
        queries += query
        return Result.success(ProductPage(all.drop(skip).take(limit), all.size))
    }

    override suspend fun product(id: Int) = Result.success(all[id])
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {
    @get:Rule val mainRule = MainDispatcherRule()

    private val repository = FakeProductRepository()
    private val viewModel by lazy { ProductListViewModel(repository) }

    @Test
    fun `loads pages until the end`() = runTest(mainRule.dispatcher) {
        viewModel
        advanceUntilIdle()
        assertEquals(20, viewModel.state.value.items.size)

        viewModel.loadMore()
        advanceUntilIdle()
        assertEquals(30, viewModel.state.value.items.size)
        assertTrue(viewModel.state.value.endReached)
    }

    @Test
    fun `fast typing searches only the last query`() = runTest(mainRule.dispatcher) {
        viewModel
        advanceUntilIdle()

        viewModel.onQueryChange("p")
        viewModel.onQueryChange("ph")
        viewModel.onQueryChange("pho")
        advanceUntilIdle()

        assertEquals(listOf("", "pho"), repository.queries)
    }
}
