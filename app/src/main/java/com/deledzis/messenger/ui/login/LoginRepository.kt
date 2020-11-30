package com.deledzis.messenger.ui.login

import com.deledzis.messenger.api.ApiInterface
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.data.model.auth.Auth
import com.deledzis.messenger.data.model.auth.AuthUserRequest

class LoginRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun login(email: String?, password: String?): Auth? {
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