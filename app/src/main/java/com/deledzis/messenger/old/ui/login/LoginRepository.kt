package com.deledzis.messenger.old.ui.login

import com.deledzis.messenger.old.data.model.auth.AuthorizedUser
import com.deledzis.messenger.presentation.base.BaseRepository

class LoginRepository(private val api: ApiInterface) : BaseRepository() {
    suspend fun login(email: String?, password: String?): AuthorizedUser? {
        return safeApiCall {
            api.authUser(
                AuthUserRequest(
                    username = email ?: "",
                    password = password ?: ""
                )
            )
        }
    }
}