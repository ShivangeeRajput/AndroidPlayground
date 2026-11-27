package com.example.androidplayground.products.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
)

data class ProductPage(val products: List<Product>, val total: Int)

interface ProductApi {
    /** An empty q returns all products, so one endpoint covers list and search. */
    @GET("products/search")
    suspend fun search(@Query("q") query: String, @Query("limit") limit: Int, @Query("skip") skip: Int): ProductPage

    @GET("products/{id}")
    suspend fun product(@Path("id") id: Int): Product
}
