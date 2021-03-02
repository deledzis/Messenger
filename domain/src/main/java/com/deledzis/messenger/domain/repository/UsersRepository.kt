package com.deledzis.messenger.domain.repository

import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.response.user.GetUserResponse
import com.deledzis.messenger.domain.model.response.user.GetUsersResponse

interface UsersRepository {

    suspend fun getUser(id: Int): Response<GetUserResponse, Error>

    suspend fun getUsers(search: String?): Response<GetUsersResponse, Error>
}