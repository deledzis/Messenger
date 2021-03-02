package com.deledzis.messenger.data.source.chats

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.chats.ChatEntity
import com.deledzis.messenger.data.model.chats.ChatsEntity
import com.deledzis.messenger.data.repository.chats.ChatsRemote
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
            if (response.errorCode == 0) {
                Response.Success(successData = response)
            } else {
                Response.Failure(
                    Error.ResponseError(
                        errorCode = response.errorCode,
                        errorMessage = response.message
                    )
                )
            }
        } catch (e: Exception) {
            Response.Failure(Error.NetworkError(exception = e))
        }
    }

    override suspend fun addChat(interlocutorId: Int): Response<ChatEntity, Error> {
        return try {
            val response = remote.addChat(interlocutorId = interlocutorId)
            if (response.errorCode == 0) {
                Response.Success(successData = response)
            } else {
                Response.Failure(
                    Error.ResponseError(
                        errorCode = response.errorCode,
                        errorMessage = response.message
                    )
                )
            }
        } catch (e: Exception) {
            Response.Failure(Error.NetworkError(exception = e))
        }
    }

}