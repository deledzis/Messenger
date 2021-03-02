package com.deledzis.messenger.domain.usecase.user

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.user.GetUsersRequest
import com.deledzis.messenger.domain.repository.UsersRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) : BaseUseCase<GetUsersRequest>() {

    override suspend fun run(params: GetUsersRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.getUsers(search = params.search)
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}