package com.deledzis.messenger.domain.usecase.auth

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.auth.RegisterRequest
import com.deledzis.messenger.domain.repository.AuthRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<RegisterRequest>() {

    override suspend fun run(params: RegisterRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.register(
            username = params.username,
            nickname = params.nickname,
            password = params.password
        )
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}