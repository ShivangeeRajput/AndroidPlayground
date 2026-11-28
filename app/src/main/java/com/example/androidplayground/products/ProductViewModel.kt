package com.example.androidplayground.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository) : ViewModel() {
    var query by mutableStateOf("")
        private set
    var products by mutableStateOf<List<Product>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var isError by mutableStateOf(false)
        private set

    private var total = 0
    private var job: Job? = null

    init {
        load()
    }

    // Agla page (retry bhi yahi hai)
    fun load() {
        job = viewModelScope.launch { fetchNextPage() }
    }

    fun loadMore() {
        if (isLoading || isError || products.size >= total) return
        load()
    }

    fun search(text: String) {
        job?.cancel() // purani request band
        query = text
        products = emptyList() // list reset
        total = 0
        isError = false
        isLoading = true
        job = viewModelScope.launch {
            delay(400) // debounce: typing rukne ka wait
            fetchNextPage()
        }
    }

    // skip = jitne products already hain
    private suspend fun fetchNextPage() {
        isLoading = true
        isError = false
        repository.getProducts(query, skip = products.size)
            .onSuccess {
                products = products + it.products
                total = it.total
            }
            .onFailure { isError = true }
        isLoading = false
    }
}
