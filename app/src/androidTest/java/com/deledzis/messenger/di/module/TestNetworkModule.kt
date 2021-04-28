package com.deledzis.messenger.di.module

import com.deledzis.messenger.common.Constants
import com.deledzis.messenger.data.repository.auth.AuthRemote
import com.deledzis.messenger.data.repository.chats.ChatsRemote
import com.deledzis.messenger.data.repository.messages.MessagesRemote
import com.deledzis.messenger.data.repository.users.UsersRemote
import com.deledzis.messenger.di.TestTokenInterceptor
import com.deledzis.messenger.remote.*
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
open class TestNetworkModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().apply {
            setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
            setLenient()
            setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
        }.create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor,
        tokenInterceptor: TestTokenInterceptor
    ): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(tokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(interceptor)
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    @Singleton
    @Provides
    fun provideRequestHeadersInterceptor(): Interceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val request: Request = original.newBuilder()
            .build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson, okHttpClientBuilder: OkHttpClient.Builder): Retrofit {
        return Retrofit.Builder()
            .baseUrl(if (Constants.MOCK_BUILD) Constants.API_BASE_URL_MOCK else Constants.API_BASE_URL_PROD)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClientBuilder.build())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiInterface(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAuthRemote(apiService: ApiService): AuthRemote {
        return AuthRemoteImpl(apiService = apiService)
    }

    @Singleton
    @Provides
    fun provideChatsRemote(apiService: ApiService): ChatsRemote {
        return ChatsRemoteImpl(apiService = apiService)
    }

    @Singleton
    @Provides
    fun provideMessagesRemote(apiService: ApiService): MessagesRemote {
        return MessagesRemoteImpl(apiService = apiService)
    }

    @Singleton
    @Provides
    fun provideUsersRemote(apiService: ApiService): UsersRemote {
        return UsersRemoteImpl(apiService = apiService)
    }

    companion object {
        private const val REQUEST_TIMEOUT = 60
    }

}