package com.deledzis.messenger.cache.preferences.user

import com.google.gson.Gson

data class AuthenticatedUser(
    val id: Int,
    val username: String,
    val nickname: String?,
    val accessToken: String
) {
    fun save(userStore: UserStore) {
        userStore.userData = Gson().toJson(this, AuthenticatedUser::class.java)
    }

    companion object {
        fun restore(userStore: UserStore): AuthenticatedUser? {
            return Gson().fromJson(userStore.userData, AuthenticatedUser::class.java)
        }

        fun clear(userStore: UserStore) {
            userStore.userData = null
        }
    }
}