package com.deledzis.messenger.remote

import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.auth.AuthEntity
import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.data.model.chats.ChatsEntity
import com.deledzis.messenger.data.model.messages.MessagesEntity
import com.deledzis.messenger.data.model.users.UserEntity
import com.deledzis.messenger.data.model.users.UsersEntity
import com.deledzis.messenger.remote.model.auth.AuthUserRequest
import com.deledzis.messenger.remote.model.auth.RegisterUserRequest
import com.deledzis.messenger.remote.model.chats.AddChatRequest
import com.deledzis.messenger.remote.model.messages.AddMessageRequest
import com.deledzis.messenger.remote.model.user.DeleteUserRequest
import com.deledzis.messenger.remote.model.user.UpdateUserRequest
import retrofit2.http.*

interface ApiService {

    /**
     * Пользователи
     **/
    // Регистрация
    @POST("users/register")
    suspend fun register(
        @Body request: RegisterUserRequest
    ): AuthEntity

    // Авторизация
    @POST("users/login")
    suspend fun login(
        @Body request: AuthUserRequest
    ): AuthEntity

    // Удалить аккаунт
    @POST("users/delete")
    suspend fun deleteAccount(
        @Body request: DeleteUserRequest
    ): ServerMessageResponseEntity

    //Изменение данных пользователя
    @PUT("users")
    suspend fun updateUserData(
        @Body request: UpdateUserRequest
    ): AuthEntity

    // Пользователь
    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): UserEntity

    // Доступные для создания чата пользователи
    @GET("users")
    suspend fun getUsersAvailable(
        @Query("search") search: String? = null
    ): UsersEntity

    /**
     * Чаты
     **/
    // Чаты
    @GET("chats")
    suspend fun getChats(): ChatsEntity

    // Добавить чат
    @POST("chats")
    suspend fun addChat(
        @Body request: AddChatRequest
    ): ChatEntity

    // Чат с фильтром сообщений
    @GET("chats/{id}")
    suspend fun getChatMessages(
        @Path("id") chatId: Int,
        @Query("search") search: String = ""
    ): MessagesEntity

    // Отправить сообщение в чат
    @POST("chats/{id}")
    suspend fun sendMessageToChat(
        @Path("id") chatId: Int,
        @Body request: AddMessageRequest
    ): ServerMessageResponseEntity

    @DELETE("users")
    suspend fun deleteUser(): ServerMessageResponseEntity
}