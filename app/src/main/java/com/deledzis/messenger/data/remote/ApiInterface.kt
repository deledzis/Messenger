package com.deledzis.messenger.data.remote

import com.deledzis.messenger.data.model.auth.Auth
import com.deledzis.messenger.data.model.auth.AuthUserRequest
import com.deledzis.messenger.data.model.auth.RegisterUserRequest
import com.deledzis.messenger.data.model.auth.UpdateUserRequest
import com.deledzis.messenger.data.model.chats.*
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.data.model.users.Users
import okhttp3.MultipartBody
import okhttp3.RequestBody
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
        @Path("id") id: Int,
        @Body request: SendMessageRequest
    ): Response<Boolean>

    @Multipart
    @POST("/chat/{id}")
    suspend fun sendPhotoToChat(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part,
        @Part("authorId") authorId: RequestBody
    ): Response<Boolean>

    // Поиск по сообщениям в чате
    @GET("/chat/{id}")
    suspend fun getMessagesByQuery(
        @Path("id") id: Int,
        @Query("search") search: String? = null
    ): Response<Messages>

    //Изменение данных пользователя
    @POST("user")
    suspend fun updateUserData(
        @Body newData: UpdateUserRequest
    ): Response<Auth>
}