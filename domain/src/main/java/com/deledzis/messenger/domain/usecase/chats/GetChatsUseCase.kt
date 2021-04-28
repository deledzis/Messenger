package com.deledzis.messenger.domain.usecase.chats

import com.deledzis.messenger.domain.repository.ChatsRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetChatsUseCase @Inject constructor(
    private val repository: ChatsRepository
) : BaseUseCase<Unit>() {

    override suspend fun run(params: Unit) {
        val response = repository.getChats()
        resultChannel.send(response)
    }
}