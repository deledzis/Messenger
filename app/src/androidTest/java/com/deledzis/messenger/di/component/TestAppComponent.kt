package com.deledzis.messenger.di.component

import com.deledzis.messenger.AuthRepositoryIntegrationTest
import com.deledzis.messenger.ChatsRepositoryIntegrationTest
import com.deledzis.messenger.MessagesRepositoryIntegrationTest
import com.deledzis.messenger.cache.di.CacheModule
import com.deledzis.messenger.data.di.RepositoriesModule
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.remote.di.NetworkModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestAppModule::class,
        NetworkModule::class,
        UtilsModule::class,
        RepositoriesModule::class,
        CacheModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class
    ]
)
interface TestAppComponent {

    fun into(authRepositoryIntegrationTest: AuthRepositoryIntegrationTest)
    fun into(chatsRepositoryIntegrationTest: ChatsRepositoryIntegrationTest)
    fun into(messagesRepositoryIntegrationTest: MessagesRepositoryIntegrationTest)

}