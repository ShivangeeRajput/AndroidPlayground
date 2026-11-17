package com.example.androidplayground.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidplayground.data.User
import com.example.androidplayground.repository.UserRepository
import kotlinx.coroutines.launch
import  androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class UserViewModel(
    private val repository: UserRepository = UserRepository()
): ViewModel() {

    var user by mutableStateOf<List<User>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        getUsers()
    }

    fun getUsers(){
        if(isLoading) return
        isLoading = true
        error = null

        viewModelScope.launch {
            try {
                val result= repository.getUsers()
                user= result
            }catch (e: Exception){
                error = "Something went wrong"
            }finally {
                isLoading = false
            }
        }
    }
}