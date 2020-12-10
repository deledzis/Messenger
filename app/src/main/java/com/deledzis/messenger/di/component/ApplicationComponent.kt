package com.deledzis.messenger.di.component

import android.content.Context
import android.content.SharedPreferences
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.data.remote.ApiInterface
import com.deledzis.messenger.di.model.UserData
import com.deledzis.messenger.di.module.ApplicationModule
import com.deledzis.messenger.di.module.ContextModule
import com.deledzis.messenger.di.module.RetrofitModule
import com.deledzis.messenger.di.qualifier.ApplicationContext
import com.deledzis.messenger.di.scopes.ApplicationScope
import com.deledzis.messenger.ui.main.MainActivity
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        ContextModule::class,
        RetrofitModule::class,
        ApplicationModule::class
    ]
)
interface ApplicationComponent {

    fun inject(application: App)

    fun inject(activity: MainActivity)

    fun inject(fragment: BaseFragment)

    fun api(): ApiInterface

    @ApplicationContext
    fun context(): Context

    fun sharedPreferences(): SharedPreferences

    fun userData(): UserData
}