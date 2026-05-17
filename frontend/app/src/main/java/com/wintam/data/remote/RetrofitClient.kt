package com.wintam.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://wintam-production.up.railway.app/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val instance: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(
            OkHttpClient.Builder()
                .addInterceptor (logging)
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun authenticatedInstance(token: String): Retrofit{
        val client= OkHttpClient.Builder()
            .addInterceptor (logging)
            .addInterceptor {chain ->
                val request= chain.request().newBuilder()
                    .addHeader("Authorization","Bearer $token")
                    .build()
                chain.proceed(request)
            }.build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}