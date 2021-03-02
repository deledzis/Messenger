package com.deledzis.messenger.domain.usecase.messages

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.messages.SendMessageRequest
import com.deledzis.messenger.domain.repository.MessagesRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: MessagesRepository
) : BaseUseCase<SendMessageRequest>() {

    override suspend fun run(params: SendMessageRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.sendMessageToChat(
            chatId = params.chatId,
            type = params.type,
            content = params.content
        )
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}