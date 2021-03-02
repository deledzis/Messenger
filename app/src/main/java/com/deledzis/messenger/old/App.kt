package com.deledzis.messenger.old

import android.app.Activity
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.deledzis.messenger.R
import com.deledzis.messenger.di.component.DaggerApplicationComponent
import com.deledzis.messenger.old.di.component.ApplicationComponent
import com.deledzis.messenger.old.di.module.ApplicationModule
import com.deledzis.messenger.old.di.module.ContextModule
import com.deledzis.messenger.old.di.module.RetrofitModule

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        injector = DaggerApplicationComponent.builder()
            .contextModule(ContextModule(this))
            .retrofitModule(RetrofitModule("${getString(R.string.base_url)}/${getString(R.string.api_version)}/"))
            .applicationModule(ApplicationModule(this))
            .build()
        injector.inject(this)

        checkNotificationChannel()
    }

    private fun checkNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.getNotificationChannel(getString(R.string.default_notification_channel_id))
                ?: createNotificationChannel()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.default_notification_channel_name)
            val description = getString(R.string.default_notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                name,
                importance
            )
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    operator fun get(activity: Activity): App {
        return activity.application as App
    }

    companion object {
        @JvmStatic
        lateinit var injector: ApplicationComponent
    }
}