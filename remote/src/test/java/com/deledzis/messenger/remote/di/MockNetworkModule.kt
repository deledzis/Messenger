package com.deledzis.messenger.remote.di

import com.deledzis.messenger.common.Constants
import com.deledzis.messenger.remote.ApiService
import com.deledzis.messenger.remote.di.NetworkModule
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MockNetworkModule {
    private fun buildGson(): Gson = GsonBuilder().apply {
        setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
        setLenient()
        setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    }.create()

    private fun buildInterceptor(): Interceptor = FakeInterceptor()

    private fun buildClient(interceptor: Interceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .connectTimeout(60.toLong(), TimeUnit.SECONDS)
        .writeTimeout(60.toLong(), TimeUnit.SECONDS)
        .readTimeout(60.toLong(), TimeUnit.SECONDS)
        .build()

    private fun buildRetrofit(gson: Gson, client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.API_BASE_URL_MOCK)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    fun buildService(): ApiService {
        val gson: Gson = buildGson()
        val interceptor: Interceptor = buildInterceptor()
        val okHttpClient: OkHttpClient = buildClient(interceptor)
        val retrofit: Retrofit = buildRetrofit(gson, okHttpClient)

        return retrofit.create(ApiService::class.java)
    }
}