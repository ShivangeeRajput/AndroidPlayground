package com.example.androidplayground.stocks.data

import kotlinx.coroutines.delay
import java.io.IOException
import kotlin.random.Random

data class Stock(val symbol: String, val name: String, val price: Double, val changePercent: Double)

/** Same shape as a Retrofit interface: swap the fake for @GET functions when a real API is given. */
interface StockApi {
    suspend fun stocks(): List<Stock>
    suspend fun stock(symbol: String): Stock
}

/** Random prices, with an occasional failure so the error handling can be demonstrated. */
class FakeStockApi : StockApi {
    private val base = listOf(
        "TCS" to ("Tata Consultancy" to 3900.0),
        "INFY" to ("Infosys" to 1500.0),
        "HDFCBANK" to ("HDFC Bank" to 1650.0),
        "RELIANCE" to ("Reliance Industries" to 2900.0),
        "ITC" to ("ITC" to 450.0),
    ).toMap()

    override suspend fun stocks(): List<Stock> {
        delay(300)
        if (Random.nextInt(8) == 0) throw IOException("Fake network error")
        return base.keys.map(::quote)
    }

    override suspend fun stock(symbol: String): Stock {
        delay(300)
        return quote(symbol)
    }

    private fun quote(symbol: String): Stock {
        val (name, price) = base[symbol] ?: throw IOException("Unknown symbol $symbol")
        val change = Random.nextDouble(-2.0, 2.0)
        return Stock(symbol, name, price * (1 + change / 100), change)
    }
}
