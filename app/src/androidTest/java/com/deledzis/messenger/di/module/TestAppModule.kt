package com.deledzis.messenger.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class TestAppModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = context
}