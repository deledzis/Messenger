package com.deledzis.messenger.di.module

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.deledzis.messenger.di.scopes.ApplicationScope
import com.deledzis.messenger.util.APP_PREFERENCES
import dagger.Module
import dagger.Provides

@Module
class ApplicationModule(private val app: Application) {
    @Provides
    @ApplicationScope
    fun appPreferences(): SharedPreferences {
        return app.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
    }
}