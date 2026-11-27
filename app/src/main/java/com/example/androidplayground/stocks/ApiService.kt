package com.example.androidplayground.stocks

import kotlinx.coroutines.delay
import kotlin.random.Random

data class Stock(val symbol: String, val price: Int)

interface ApiService {
    suspend fun getStocks(): List<Stock>
}

// Nakli API: har call pe naya random price. Real API mile to Retrofit interface bana do.
class FakeApiService : ApiService {
    override suspend fun getStocks(): List<Stock> {
        delay(300)
        return listOf("TCS", "INFY", "ITC", "RELIANCE").map { Stock(it, Random.nextInt(100, 4000)) }
    }
}
