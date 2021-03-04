package com.deledzis.messenger.domain.usecase.chats

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.repository.ChatsRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val repository: ChatsRepository
) : BaseUseCase<Unit>() {

    override suspend fun run(params: Unit) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.getChats()
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}