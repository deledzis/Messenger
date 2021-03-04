package com.deledzis.messenger

import android.content.Context
import androidx.multidex.MultiDex
import com.deledzis.messenger.di.component.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class App : DaggerApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()

        // Initializing logger
        setupTimber()
    }

    override fun applicationInjector(): AndroidInjector<out App> =
        DaggerApplicationComponent.factory().create(this)

    private fun setupTimber() {
        Timber.uprootAll()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}