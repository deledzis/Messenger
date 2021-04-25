package com.deledzis.messenger.data.repository.messages

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.mapper.ServerMessageResponseMapper
import com.deledzis.messenger.data.mapper.messages.MessagesMapper
import com.deledzis.messenger.data.source.messages.MessagesDataStoreFactory
import com.deledzis.messenger.domain.model.BaseNetworkManager
import com.deledzis.messenger.domain.model.response.messages.DeleteMessageResponse
import com.deledzis.messenger.domain.model.response.messages.GetChatMessagesResponse
import com.deledzis.messenger.domain.model.response.messages.SendMessageResponse
import com.deledzis.messenger.domain.repository.MessagesRepository

/**
 * Provides an implementation of the [MessagesRepository] interface for communicating to and from
 * data sources
 */
class MessagesRepositoryImpl(
    private val factory: MessagesDataStoreFactory,
    private val itemsMapper: MessagesMapper,
    private val serverMessageResponseMapper: ServerMessageResponseMapper,
    private val networkManager: BaseNetworkManager
) : MessagesRepository {

    override suspend fun getChatMessages(
        chatId: Int,
        search: String
    ): Response<GetChatMessagesResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().getChatMessages(
                chatId = chatId,
                search = search
            )
            var response: Response<GetChatMessagesResponse, Error> =
                Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        GetChatMessagesResponse(response = itemsMapper.mapFromEntity(it))
                    )
                },
                failureBlock = { response = Response.Failure(it) }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }

    override suspend fun sendMessageToChat(
        chatId: Int,
        type: Int,
        content: String
    ): Response<SendMessageResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().sendMessage(
                chatId = chatId,
                type = type,
                content = content
            )
            var response: Response<SendMessageResponse, Error> =
                Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        SendMessageResponse(
                            response = serverMessageResponseMapper.mapFromEntity(it)
                        )
                    )
                },
                failureBlock = { response = Response.Failure(it) }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }

    override suspend fun deleteMessage(messageId: Int): Response<DeleteMessageResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().deleteMessage(messageId)
            var response: Response<DeleteMessageResponse, Error> =
                Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        DeleteMessageResponse(
                            response = serverMessageResponseMapper.mapFromEntity(it)
                        )
                    )
                },
                failureBlock = { response = Response.Failure(it) }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }

}