package com.deledzis.messenger.domain.model.entity.user

import com.deledzis.messenger.domain.model.entity.auth.Auth
import javax.inject.Singleton

@Singleton
abstract class BaseUserData {
    abstract fun getAuthUser(): Auth?

    abstract fun saveAuthUser(auth: Auth?): Boolean

    inline fun doAndSaveAuthUser(auth: Auth?, block: () -> Unit) {
        block()
        saveAuthUser(auth)
    }
}