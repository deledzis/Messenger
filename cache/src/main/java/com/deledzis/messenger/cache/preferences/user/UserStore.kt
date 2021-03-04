package com.deledzis.messenger.cache.preferences.user

import android.content.SharedPreferences
import com.deledzis.messenger.cache.preferences.PreferencesDelegate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserStore @Inject constructor(preferences: SharedPreferences) {

    var userData: String? by PreferencesDelegate(
        preferences,
        PREF_KEY_USER_DATA,
        ""
    )

    companion object {
        private const val PREF_KEY_USER_DATA = "user_data"
    }
}