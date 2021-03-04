package com.deledzis.messenger.domain.usecase.chats

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.chats.AddChatRequest
import com.deledzis.messenger.domain.repository.ChatsRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class AddChatUseCase @Inject constructor(
    private val repository: ChatsRepository
) : BaseUseCase<AddChatRequest>() {

    override suspend fun run(params: AddChatRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.addChat(
            interlocutorId = params.interlocutorId
        )
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}