package com.deledzis.messenger.data.source.chats

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.data.model.chats.ChatsEntity
import com.deledzis.messenger.data.repository.chats.ChatsRemote
import retrofit2.HttpException
import javax.inject.Inject

/**
 * Implementation of the [ChatsDataStore] interface to provide a means of communicating
 * with the remote data source
 */
class ChatsRemoteDataStore @Inject constructor(private val remote: ChatsRemote) :
    ChatsDataStore {

    override suspend fun getChats(): Response<ChatsEntity, Error> {
        return try {
            val response = remote.getChats()
            Response.Success(successData = response)
        } catch (e: Exception) {
            if (e is HttpException) Response.Failure(Error.ResponseError(errorCode = e.code()))
            else Response.Failure(Error.NetworkError())
        }
    }

    override suspend fun addChat(interlocutorId: Int): Response<ChatEntity, Error> {
        return try {
            val response = remote.addChat(interlocutorId = interlocutorId)
            Response.Success(successData = response)
        } catch (e: Exception) {
            if (e is HttpException) Response.Failure(Error.ResponseError(errorCode = e.code()))
            else Response.Failure(Error.NetworkError())
        }
    }

}