package com.deledzis.messenger.di.module

import android.content.Context
import com.deledzis.messenger.di.qualifier.ApplicationContext
import com.deledzis.messenger.di.scopes.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideContext(): Context {
        return context
    }
}
