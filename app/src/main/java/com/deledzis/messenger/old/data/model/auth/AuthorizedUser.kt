package com.deledzis.messenger.old.data.model.auth

import android.content.SharedPreferences
import com.deledzis.messenger.old.data.store.UserStore
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class AuthorizedUser(
    val id: Int,
    val username: String,
    val nickname: String,
    @SerializedName("accessToken")
    val accessToken: String?
) {
    fun save(preferences: SharedPreferences) {
        val store = UserStore(preferences)
        store.userData = Gson().toJson(this, AuthorizedUser::class.java)
    }

    companion object {
        fun restore(preferences: SharedPreferences): AuthorizedUser? {
            val store = UserStore(preferences)
            return Gson().fromJson(store.userData, AuthorizedUser::class.java)
        }

        fun clear(preferences: SharedPreferences) {
            UserStore(preferences).userData = null
        }
    }
}