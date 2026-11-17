package com.example.androidplayground.repository

import com.example.androidplayground.data.ApiService
import com.example.androidplayground.data.RetrofitClient
import com.example.androidplayground.data.User

class UserRepository(
    private val apiService: ApiService = RetrofitClient.api
) {
    suspend fun getUsers() : List<User> = apiService.getUsers()
}