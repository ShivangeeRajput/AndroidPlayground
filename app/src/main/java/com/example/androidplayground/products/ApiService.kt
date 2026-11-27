package com.example.androidplayground.products

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// JSON ke fields ke naam same rakho, Gson khud convert kar deta hai
data class Product(val id: Int, val title: String, val description: String, val price: Double)

data class ProductResponse(val products: List<Product>, val total: Int)

interface ApiService {
    // query khaali ho to saare products aate hain
    @GET("products/search")
    suspend fun getProducts(
        @Query("q") query: String,
        @Query("skip") skip: Int,
        @Query("limit") limit: Int = 20,
    ): ProductResponse

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product
}
