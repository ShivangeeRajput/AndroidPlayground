package com.example.androidplayground.products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailState(val product: Product? = null, val error: String? = null)

class DetailViewModel(private val repository: ProductRepository) : ViewModel() {
    private val _state = MutableStateFlow(DetailState())
    val state = _state.asStateFlow()

    fun load(id: Int) {
        _state.value = DetailState() // product null + error null = loading
        viewModelScope.launch {
            repository.getProduct(id)
                .onSuccess { _state.value = DetailState(product = it) }
                .onFailure { _state.value = DetailState(error = "Could not load product") }
        }
    }
}
