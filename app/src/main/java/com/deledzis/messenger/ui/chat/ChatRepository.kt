package com.deledzis.messenger.ui.chat

import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.cache.DataSourceFactory
import com.deledzis.messenger.data.model.BaseResponse
import com.deledzis.messenger.data.model.chats.Chat

class ChatRepository : BaseRepository() {
    suspend fun getChat(id: Int): Chat? {
        val respond: Chat?
        if (networkManager.isConnectedToInternet) {
            respond = safeApiCall { DataSourceFactory().getRemote().getChat(chatId = id) }
            if (respond != null)
                DataSourceFactory().getCache().updateMessages(respond.messages)
        } else
            respond = DataSourceFactory().getCache().getChat(chatId = id)
        return respond
    }

    suspend fun sendTextMessage(
        chatId: Int,
        authorId: Int,
        type: Boolean,
        content: String
    ): BaseResponse? {
        return safeApiCall {
            DataSourceFactory().getRemote().sendTextMessage(
                chatId = chatId,
                authorId = authorId,
                type = type,
                content = content
            )
        }
    }
}