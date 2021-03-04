package com.deledzis.messenger.di.component

import com.deledzis.messenger.App
import com.deledzis.messenger.cache.di.CacheModule
import com.deledzis.messenger.data.di.RepositoriesModule
import com.deledzis.messenger.di.module.ApplicationModule
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.presentation.di.builder.MainActivityBuilder
import com.deledzis.messenger.presentation.di.module.ViewModelModule
import com.deledzis.messenger.remote.di.NetworkModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        NetworkModule::class,
        UtilsModule::class,
        RepositoriesModule::class,
        CacheModule::class,
        MainActivityBuilder::class,
        ViewModelModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class
    ]
)
interface ApplicationComponent : AndroidInjector<App> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<App>
}