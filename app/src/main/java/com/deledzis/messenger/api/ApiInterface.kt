package com.deledzis.messenger.api

import com.deledzis.messenger.data.model.auth.Auth
import com.deledzis.messenger.data.model.auth.AuthUserRequest
import com.deledzis.messenger.data.model.auth.RegisterUserRequest
import com.deledzis.messenger.data.model.chats.Chats
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    /**
     * Авторизация
     **/
    // Регистрация
    @POST("register")
    suspend fun registerUser(
        @Body request: RegisterUserRequest
    ): Response<Auth>

    // Авторизация
    @POST("login")
    suspend fun authUser(
        @Body request: AuthUserRequest
    ): Response<Auth>

    // Авторизация
    @GET("chats")
    suspend fun getChats(): Response<Chats>
}