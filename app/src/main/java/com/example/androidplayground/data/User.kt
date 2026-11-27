package com.example.androidplayground.data

data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val gender: String,
)

data class UsersResponse(
    val users: List<User>,
    val total: Int,
    val skip: Int,
    val limit: Int,
)
