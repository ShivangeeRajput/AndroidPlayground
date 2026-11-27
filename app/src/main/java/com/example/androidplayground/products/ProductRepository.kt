package com.example.androidplayground.products

import com.example.androidplayground.core.safeCall

// Data kahan se aayega, ye sirf Repository jaanta hai
class ProductRepository(private val api: ApiService) {
    // safeCall = try/catch, jo Result deta hai
    suspend fun getProducts(query: String, skip: Int) = safeCall { api.getProducts(query, skip) }

    suspend fun getProduct(id: Int) = safeCall { api.getProduct(id) }
}
