package com.deledzis.messenger.cache.preferences.user

import com.deledzis.messenger.cache.mapper.UserMapper
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import timber.log.Timber

class UserData(
    private val userStore: UserStore,
    private val userMapper: UserMapper
) : BaseUserData() {
    var user: AuthenticatedUser? = AuthenticatedUser.restore(userStore = userStore)
        set(value) {
            Timber.d("Setting AuthenticatedUser: $value")
            field = value
            if (value == null) {
                AuthenticatedUser.clear(userStore = userStore)
            } else {
                value.save(userStore = userStore)
            }
        }

    override fun getAuthUser(): Auth? {
        return this.user?.let { userMapper.mapFromEntity(it) }
    }

    override fun saveAuthUser(auth: Auth?): Boolean {
        Timber.d("Saving User: $user")
        this.user = auth?.let { userMapper.mapToEntity(it) }
        return true
    }
}