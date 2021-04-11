package com.deledzis.messenger.di.module

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.deledzis.messenger.App
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class ApplicationModule {

    @Binds
    abstract fun bindApplication(app: App): Application

    @SuppressLint("ModuleCompanionObjects")
    @Module
    companion object {
        @Singleton
        @Provides
        fun provideContext(application: Application): Context {
            return application.applicationContext
        }
    }
}