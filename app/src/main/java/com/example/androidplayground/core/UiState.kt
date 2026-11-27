package com.example.androidplayground.core

/** Use for one-shot screens (e.g. detail). Lists with paging need their own state class. */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
