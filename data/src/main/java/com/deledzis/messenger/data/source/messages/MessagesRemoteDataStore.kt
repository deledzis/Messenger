package com.deledzis.messenger.data.source.messages

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.model.ServerMessageResponseEntity
import com.deledzis.messenger.data.model.messages.MessagesEntity
import com.deledzis.messenger.data.repository.messages.MessagesRemote
import javax.inject.Inject

/**
 * Implementation of the [MessagesDataStore] interface to provide a means of communicating
 * with the remote data source
 */
open class MessagesRemoteDataStore @Inject constructor(private val remote: MessagesRemote) :
    MessagesDataStore {

    override suspend fun getChatMessages(
        chatId: Int,
        search: String?
    ): Response<MessagesEntity, Error> {
        return try {
            val response = remote.getChatMessages(
                chatId = chatId,
                search = search
            )
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

    override suspend fun sendMessage(
        chatId: Int,
        type: Int,
        content: String
    ): Response<ServerMessageResponseEntity, Error> {
        return try {
            val response = remote.sendMessage(
                chatId = chatId,
                type = type,
                content = content
            )
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