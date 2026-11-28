package com.example.androidplayground.products

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: ProductRepository) : ViewModel() {
    var product by mutableStateOf<Product?>(null)
        private set
    var isError by mutableStateOf(false)
        private set

    // product null aur isError false = loading
    fun load(id: Int) {
        isError = false
        viewModelScope.launch {
            repository.getProduct(id)
                .onSuccess { product = it }
                .onFailure { isError = true }
        }
    }
}
