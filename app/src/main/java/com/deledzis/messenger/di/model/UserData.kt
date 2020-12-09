package com.deledzis.messenger.di.model

import android.content.SharedPreferences
import com.deledzis.messenger.data.model.auth.AuthorizedUser
import com.deledzis.messenger.di.scopes.ApplicationScope
import javax.inject.Inject

@ApplicationScope
class UserData @Inject constructor(private val preferences: SharedPreferences) {
    var authorizedUser: AuthorizedUser? = AuthorizedUser.restore(preferences)
        set(value) {
            field = value
            if (value == null) {
                AuthorizedUser.clear(preferences)
            } else {
                value.save(preferences)
            }
        }
}