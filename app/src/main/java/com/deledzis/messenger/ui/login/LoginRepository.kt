package com.deledzis.messenger.ui.login

import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.auth.AuthUserRequest
import com.deledzis.messenger.data.model.auth.AuthorizedUser
import com.deledzis.messenger.data.remote.ApiInterface

class LoginRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun login(email: String?, password: String?): AuthorizedUser? {
        return safeApiCall(
            call = {
                api.authUser(
                    AuthUserRequest(
                        username = email ?: "",
                        password = password ?: ""
                    )
                )
            },
            errorMessage = "Error while login"
        )
    }
}