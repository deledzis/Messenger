package com.deledzis.messenger.domain.usecase.chats

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.chats.AddChatRequest
import com.deledzis.messenger.domain.model.request.chats.DeleteChatRequest
import com.deledzis.messenger.domain.repository.ChatsRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class DeleteChatUseCase @Inject constructor(
    private val repository: ChatsRepository
) : BaseUseCase<DeleteChatRequest>() {

    override suspend fun run(params: DeleteChatRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.deleteChat(params.chatId)
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}