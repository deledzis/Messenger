package com.deledzis.messenger.api

import com.deledzis.messenger.data.model.auth.Auth
import com.deledzis.messenger.data.model.auth.AuthUserRequest
import com.deledzis.messenger.data.model.auth.RegisterUserRequest
import com.deledzis.messenger.data.model.chats.Chat
import com.deledzis.messenger.data.model.chats.Chats
import com.deledzis.messenger.data.model.chats.SendMessageRequest
import com.deledzis.messenger.data.model.user.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    // Пользователь
    @GET("user/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): Response<User>

    // Чаты
    @GET("chats")
    suspend fun getChats(): Response<Chats>

    // Чат
    @GET("chat/{id}")
    suspend fun getChat(
        @Path("id") id: Int
    ): Response<Chat>

    // Отправить сообщение в чат
    @POST("/chat/{id}")
    suspend fun sendMessageToChat(
        @Body request: SendMessageRequest
    ): Response<Any>
}