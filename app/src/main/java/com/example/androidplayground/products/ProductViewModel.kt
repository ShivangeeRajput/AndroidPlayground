package com.example.androidplayground.products

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProductState(
    val query: String = "",
    val products: List<Product> = emptyList(),
    val total: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProductState(query = repository.lastQuery))
    val state = _state.asStateFlow()

    private var job: Job? = null

    init {
        load()
    }

    // Agla page laata hai. skip = jitne products already hain
    fun load(waitMillis: Long = 0) {
        _state.update { it.copy(isLoading = true, error = null) }
        job = viewModelScope.launch {
            delay(waitMillis)
            repository.getProducts(_state.value.query, skip = _state.value.products.size)
                .onSuccess { response ->
                    _state.update {
                        it.copy(products = it.products + response.products, total = response.total, isLoading = false)
                    }
                }
                .onFailure { e ->
                    Log.e("Products", "load failed", e)
                    _state.update { it.copy(isLoading = false, error = "Something went wrong") }
                }
        }
    }

    fun loadMore() {
        val s = _state.value
        if (s.isLoading || s.error != null || s.products.size >= s.total) return
        load()
    }

    fun search(query: String) {
        job?.cancel() // purani request band
        repository.lastQuery = query
        _state.value = ProductState(query = query) // list reset
        load(waitMillis = 400) // debounce: typing rukne ka wait
    }
}
