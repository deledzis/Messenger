package com.deledzis.messenger.remote

import com.deledzis.messenger.common.Constants
import com.deledzis.messenger.remote.di.NetworkModule
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MockNetworkModule {
    fun buildClient() : ApiService {

        val gson = GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            setLenient()
            setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        }.create()

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(FakeInterceptor())
            .connectTimeout(60.toLong(), TimeUnit.SECONDS)
            .writeTimeout(60.toLong(), TimeUnit.SECONDS)
            .readTimeout(60.toLong(), TimeUnit.SECONDS)

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL_MOCK)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClientBuilder.build())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}