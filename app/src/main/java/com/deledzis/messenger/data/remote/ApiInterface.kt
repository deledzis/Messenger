package com.deledzis.messenger.data.remote

import com.deledzis.messenger.data.model.auth.AuthUserRequest
import com.deledzis.messenger.data.model.auth.AuthorizedUser
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
     * Пользователи
     **/
    // Регистрация
    @POST("users/register")
    suspend fun registerUser(
        @Body request: RegisterUserRequest
    ): Response<AuthorizedUser>

    // Авторизация
    @POST("users/login")
    suspend fun authUser(
        @Body request: AuthUserRequest
    ): Response<AuthorizedUser>

    //Изменение данных пользователя
    @PUT("users")
    suspend fun updateUserData(
        @Body newData: UpdateUserRequest
    ): Response<AuthorizedUser>

    // Пользователь
    @GET("users/{id}")
    suspend fun getUser(
        @Path("id") id: Int
    ): Response<User>

    // Доступные для создания чата пользователи
    @GET("users")
    suspend fun getUsersAvailable(
        @Query("search") search: String? = null
    ): Response<Users>

    /**
    * Чаты
    **/
    // Чаты
    @GET("chats")
    suspend fun getChats(): Response<Chats>

    // Добавить чат
    @POST("chats")
    suspend fun addChat(
        @Body request: AddChatRequest
    ): Response<ChatReduced>

    // Чат
    @GET("chats/{id}")
    suspend fun getChat(
        @Path("id") id: Int
    ): Response<Chat>

    // Отправить сообщение в чат
    @POST("chats/{id}")
    suspend fun sendMessageToChat(
        @Path("id") id: Int,
        @Body request: SendMessageRequest
    ): Response<Boolean>

    @Multipart
    @POST("chats/{id}")
    suspend fun sendPhotoToChat(
        @Path("id") id: Int,
        @Part image: MultipartBody.Part,
        @Part("authorId") authorId: RequestBody
    ): Response<Boolean>

    // Поиск по сообщениям в чате
    @GET("chats/{id}")
    suspend fun getMessagesByQuery(
        @Path("id") id: Int,
        @Query("search") search: String? = null
    ): Response<Messages>
}