package com.example.androidplayground.products.data

import com.example.androidplayground.core.safeCall

interface ProductRepository {
    suspend fun search(query: String, skip: Int, limit: Int): Result<ProductPage>
    suspend fun product(id: Int): Result<Product>
}

class ProductRepositoryImpl(private val api: ProductApi) : ProductRepository {
    override suspend fun search(query: String, skip: Int, limit: Int) = safeCall { api.search(query, limit, skip) }
    override suspend fun product(id: Int) = safeCall { api.product(id) }
}
