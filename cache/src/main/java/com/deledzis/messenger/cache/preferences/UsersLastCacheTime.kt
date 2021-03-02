package com.deledzis.messenger.cache.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersLastCacheTime @Inject constructor(preferences: SharedPreferences) {

    var lastCacheTime: Long? by PreferencesDelegate(
        preferences,
        PREF_KEY_LAST_CACHE_TIME,
        0L
    )

    companion object {
        private const val PREF_KEY_LAST_CACHE_TIME = "users_last_cache_time"
    }
}