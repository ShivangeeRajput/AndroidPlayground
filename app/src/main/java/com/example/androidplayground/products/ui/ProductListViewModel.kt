package com.example.androidplayground.products.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplayground.products.data.Product
import com.example.androidplayground.products.data.ProductRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductListState(
    val query: String = "",
    val items: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val endReached: Boolean = false,
)

class ProductListViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProductListState())
    val state: StateFlow<ProductListState> = _state.asStateFlow()

    /** The one running request (or debounce wait). Cancelling it drops stale search results. */
    private var job: Job? = null

    init {
        load()
    }

    fun onQueryChange(query: String) {
        if (query == _state.value.query) return
        job?.cancel()
        _state.value = ProductListState(query = query, isLoading = true)
        load(debounceMillis = DEBOUNCE_MILLIS)
    }

    /** Called when the user scrolls near the bottom. */
    fun loadMore() {
        val s = _state.value
        if (s.isLoading || s.endReached || s.error != null) return
        load()
    }

    fun retry() = load()

    private fun load(debounceMillis: Long = 0) {
        _state.update { it.copy(isLoading = true, error = null) }
        job = viewModelScope.launch {
            delay(debounceMillis)
            val current = _state.value
            repository.search(current.query, skip = current.items.size, limit = PAGE_SIZE)
                .onSuccess { page ->
                    _state.update {
                        val items = (it.items + page.products).distinctBy { p -> p.id }
                        it.copy(
                            items = items,
                            isLoading = false,
                            endReached = page.products.isEmpty() || items.size >= page.total,
                        )
                    }
                }
                .onFailure { e ->
                    Log.e("Products", "load failed", e)
                    _state.update { it.copy(isLoading = false, error = "Something went wrong") }
                }
        }
    }

    companion object {
        const val PAGE_SIZE = 20
        const val DEBOUNCE_MILLIS = 400L
    }
}
