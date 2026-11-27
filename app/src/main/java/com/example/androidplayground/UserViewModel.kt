package com.example.androidplayground

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplayground.data.User
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val repository = UserRepository()

    var users by mutableStateOf<List<User>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    private var page = 1
    private val limit = 20
    private var endReached = false

    init {
        loadNextPage()
    }

    fun loadNextPage() {
        if (isLoading || endReached) return
        isLoading = true
        error = null

        viewModelScope.launch {
            repository.getUsers(page = page, limit = limit)
                .onSuccess {
                    users = users + it.users
                    endReached = users.size >= it.total
                    page++ // only advance on success, so a failed call retries the same page
                }
                .onFailure { error = it.message }
            isLoading = false
        }
    }
}
