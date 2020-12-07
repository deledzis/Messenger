package com.deledzis.messenger.api

import com.deledzis.messenger.data.model.auth.Auth
import com.deledzis.messenger.data.model.auth.AuthUserRequest
import com.deledzis.messenger.data.model.auth.RegisterUserRequest
import com.deledzis.messenger.data.model.chats.*
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.data.model.users.Users
import retrofit2.Response
import retrofit2.http.*

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

    // Доступные для создания чата пользователи
    @GET("users/available")
    suspend fun getUsersAvailable(
        @Query("search") search: String? = null
    ): Response<Users>

    // Чаты
    @GET("chats")
    suspend fun getChats(): Response<Chats>

    // Добавить чат
    @POST("chats")
    suspend fun addChat(
        @Body request: AddChatRequest
    ): Response<ChatReduced>

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