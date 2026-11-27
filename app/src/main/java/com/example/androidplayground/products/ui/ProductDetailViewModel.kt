package com.example.androidplayground.products.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplayground.core.UiState
import com.example.androidplayground.products.data.Product
import com.example.androidplayground.products.data.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** "id" comes from the navigation route "detail/{id}" and survives process death. */
class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ProductRepository,
) : ViewModel() {
    private val id: Int = checkNotNull(savedStateHandle["id"])

    private val _state = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val state: StateFlow<UiState<Product>> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = repository.product(id).fold(
                onSuccess = { UiState.Success(it) },
                onFailure = { UiState.Error("Could not load product") },
            )
        }
    }
}
