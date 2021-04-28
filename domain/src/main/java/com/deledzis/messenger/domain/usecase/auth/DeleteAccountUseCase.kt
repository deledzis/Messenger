package com.deledzis.messenger.domain.usecase.auth

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.auth.DeleteAccountRequest
import com.deledzis.messenger.domain.repository.AuthRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class DeleteAccountUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<DeleteAccountRequest>() {

    override suspend fun run(params: DeleteAccountRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.deleteAccount(params.username)
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}