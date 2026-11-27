package com.example.androidplayground

import com.example.androidplayground.data.RetrofitClient
import com.example.androidplayground.data.UsersResponse

class UserRepository {
    // Callers think in pages (1-based). dummyjson only understands skip,
    // so we convert here: page 1 -> skip 0, page 2 -> skip = limit, ...
    suspend fun getUsers(page: Int, limit: Int): Result<UsersResponse> =
        runCatching { RetrofitClient.api.getUsers(limit = limit, skip = (page - 1) * limit) }
}
