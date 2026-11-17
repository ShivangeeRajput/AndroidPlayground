package com.example.androidplayground.data

import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers() : List<User>
}

