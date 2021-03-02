package com.deledzis.messenger.old.data.store

import android.content.SharedPreferences

class UserStore(preferences: SharedPreferences) {

    var userData: String? by PreferencesDelegate(preferences, USER_DATA, "")

    companion object {
        private const val USER_DATA = "user_data"
    }
}