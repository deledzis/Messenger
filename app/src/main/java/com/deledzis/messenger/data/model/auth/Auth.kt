package com.deledzis.messenger.data.model.auth

import android.content.SharedPreferences
import com.deledzis.messenger.data.store.UserStore
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Auth(
    val token: String?,
    @SerializedName("user_id")
    val userId: Int,
    val username: String,
    val nickname: String
) {
    fun save(preferences: SharedPreferences) {
        val store = UserStore(preferences)
        store.userData = Gson().toJson(this, Auth::class.java)
    }

    companion object {
        fun restore(preferences: SharedPreferences): Auth? {
            val store = UserStore(preferences)
            return Gson().fromJson(store.userData, Auth::class.java)
        }

        fun clear(preferences: SharedPreferences) {
            UserStore(preferences).userData = null
        }
    }
}