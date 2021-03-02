package com.deledzis.messenger.domain.usecase.auth

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.auth.LoginRequest
import com.deledzis.messenger.domain.repository.AuthRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<LoginRequest>() {

    override suspend fun run(params: LoginRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.login(
            username = params.username,
            password = params.password
        )
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}