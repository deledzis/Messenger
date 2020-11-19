package com.deledzis.messenger.di.model

import android.content.SharedPreferences
import com.deledzis.messenger.data.model.auth.Auth
import com.deledzis.messenger.di.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class UserData @Inject constructor(private val preferences: SharedPreferences) {
    var auth: Auth? = Auth.restore(preferences)
        set(value) {
            field = value
            if (value == null) {
                Auth.clear(preferences)
            } else {
                value.save(preferences)
            }
        }
}