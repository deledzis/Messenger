package com.deledzis.messenger.domain.usecase.messages

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.messages.GetChatMessagesRequest
import com.deledzis.messenger.domain.repository.MessagesRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetChatMessagesUseCase @Inject constructor(
    private val repository: MessagesRepository
) : BaseUseCase<GetChatMessagesRequest>() {

    override suspend fun run(params: GetChatMessagesRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.getChatMessages(
            chatId = params.chatId,
            search = params.search
        )
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}