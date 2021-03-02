package com.deledzis.messenger.domain.usecase.auth

import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.request.auth.UpdateUserDataRequest
import com.deledzis.messenger.domain.repository.AuthRepository
import com.deledzis.messenger.domain.usecase.BaseUseCase
import javax.inject.Inject

class UpdateUserDataUseCase @Inject constructor(
    private val repository: AuthRepository
) : BaseUseCase<UpdateUserDataRequest>() {

    override suspend fun run(params: UpdateUserDataRequest) {
        // Started loading
        resultChannel.send(Response.State.Loading())

        // synchronous
        val response = repository.updateUserData(
            username = params.username,
            nickname = params.nickname,
            password = params.password,
            newPassword = params.newPassword
        )
        resultChannel.send(response)
        resultChannel.send(Response.State.Loaded())
    }
}