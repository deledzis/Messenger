package com.deledzis.messenger.data.repository.chats

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.mapper.ServerMessageResponseMapper
import com.deledzis.messenger.data.mapper.chats.ChatMapper
import com.deledzis.messenger.data.mapper.chats.ChatsMapper
import com.deledzis.messenger.data.source.chats.ChatsDataStoreFactory
import com.deledzis.messenger.domain.model.BaseNetworkManager
import com.deledzis.messenger.domain.model.response.chats.AddChatResponse
import com.deledzis.messenger.domain.model.response.chats.DeleteChatResponse
import com.deledzis.messenger.domain.model.response.chats.GetChatsResponse
import com.deledzis.messenger.domain.repository.ChatsRepository

/**
 * Provides an implementation of the [ChatsRepository] interface for communicating to and from
 * data sources
 */
class ChatsRepositoryImpl(
    private val factory: ChatsDataStoreFactory,
    private val itemMapper: ChatMapper,
    private val itemsMapper: ChatsMapper,
    private val serverMessageResponseMapper: ServerMessageResponseMapper,
    private val networkManager: BaseNetworkManager
) : ChatsRepository {

    override suspend fun getChats(): Response<GetChatsResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().getChats()
            var response: Response<GetChatsResponse, Error> = Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        GetChatsResponse(response = itemsMapper.mapFromEntity(it))
                    )
                },
                failureBlock = { response = Response.Failure(it) }
            )
            response
        } else {
            Response.Failure(Error.NetworkConnectionError())
        }
    }

    override suspend fun addChat(interlocutorId: Int): Response<AddChatResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().addChat(interlocutorId = interlocutorId)
            var response: Response<AddChatResponse, Error> = Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        AddChatResponse(
                            response = it.let { itemMapper.mapFromEntity(it) }
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

    override suspend fun deleteChat(chatId: Int): Response<DeleteChatResponse, Error> {
        return if (networkManager.isConnectedToInternet()) {
            val result = factory.retrieveDataStore().deleteChat(chatId)
            var response: Response<DeleteChatResponse, Error> = Response.Failure(Error.NetworkError())
            result.handleResult(
                stateBlock = { response = it },
                successBlock = {
                    response = Response.Success(
                        DeleteChatResponse(response = it.let { serverMessageResponseMapper.mapFromEntity(it) })
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