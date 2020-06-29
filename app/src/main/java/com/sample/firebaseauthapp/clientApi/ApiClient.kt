package com.sample.firebaseauthapp.clientApi

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    object Client {
        const val BASE_URL = "https://www.universal-tutorial.com/api/"
        const val BASE_Weather_URL = "http://api.openweathermap.org/"
        private var retrofit: Retrofit? = null

        open fun getClient(): Retrofit? {
            val okHttpClientBuilder = OkHttpClient.Builder()
            okHttpClientBuilder.addInterceptor { chain ->
                val original = chain.request()

                val request = original.newBuilder()
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJfZW1haWwiOiJwYXZpdGhyYXJhZGhha3Jpc2huYW45NUBnbWFpbC5jb20iLCJhcGlfdG9rZW4iOiJLUXpGaHY1ZmJCTEpxZVZiSW50RDBBVGhQVmhHNFdwU0M5T0hFdVZtYmFIZTBoclZmMEVydDJBNzViZWxYdVc4QjlFIn0sImV4cCI6MTU5MTcwNzU4NH0.WjiJs4eSfuPvRld_Vh858sftHkOAPp-xpShI3pHlL28")
                    .header("Accept","application/json")
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
            val client =
                okHttpClientBuilder.connectTimeout(5, TimeUnit.MINUTES)
                    .writeTimeout(5, TimeUnit.MINUTES)
                    .readTimeout(5, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .build()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }

        //this is because we having two baseurl and tow request so we have initialise two retrofit otherwise we
        //we can use only for whole app.
        open fun getClientWeatherAPI(): Retrofit? {
            val okHttpClientBuilder = OkHttpClient.Builder()
            okHttpClientBuilder.addInterceptor { chain ->
                val original = chain.request()

                val request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
            val client =
                okHttpClientBuilder.connectTimeout(10, TimeUnit.MINUTES)
                    .writeTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(10, TimeUnit.MINUTES)
                    .retryOnConnectionFailure(true)
                    .build()
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_Weather_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit
        }
    }
}