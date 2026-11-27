package com.example.androidplayground.data

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    // GET users?limit=10&skip=20
    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
    ): UsersResponse
}

interface ApiServicee{
    suspend fun getUsersData(
        @Query("limit") limit: Int,
        @Query("Page") page: Int,
    ): UsersResponse
}