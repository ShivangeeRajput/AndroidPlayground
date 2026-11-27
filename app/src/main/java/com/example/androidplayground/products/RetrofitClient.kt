package com.example.androidplayground.products

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val api: ApiService = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/") // end mein / zaroori hai
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
