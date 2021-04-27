package com.deledzis.messenger.domain.usecase.messages

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.messages.DeleteMessageRequest
import com.deledzis.messenger.domain.model.request.messages.SendMessageRequest
import com.deledzis.messenger.domain.repository.MessagesRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val repository: MessagesRepository
) : BaseUseCase<DeleteMessageRequest>() {

    override suspend fun run(params: DeleteMessageRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.deleteMessage(params.messageId)
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}