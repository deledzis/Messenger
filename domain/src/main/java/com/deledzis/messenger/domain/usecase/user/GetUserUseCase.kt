package com.deledzis.messenger.domain.usecase.user

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.user.GetUserRequest
import com.deledzis.messenger.domain.repository.UsersRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UsersRepository
) : BaseUseCase<GetUserRequest>() {

    override suspend fun run(params: GetUserRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.getUser(id = params.id)
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}