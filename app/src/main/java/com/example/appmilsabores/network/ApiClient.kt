package com.example.appmilsabores.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    // For AVD emulator use 10.0.2.2:8080 -> Spring Boot typically runs on 8080
    private const val BASE_URL = "http://10.0.2.2:8080/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Development-only interceptor: logs request URL + headers and response body when status is not 2xx
    private val debugResponseInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code !in 200..299) {
            try {
                val peek = response.peekBody(1024 * 1024).string()
                Log.e("ApiClient", "HTTP ${response.code} ${request.method} ${request.url}\nRequest headers: ${request.headers}\nResponse body: $peek")
            } catch (t: Throwable) {
                Log.e("ApiClient", "Error reading response body for debug logging", t)
            }
        }

        response
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .addInterceptor(debugResponseInterceptor)
        .build()

    val service: ApiService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ApiService::class.java)
}
