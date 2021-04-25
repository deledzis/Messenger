package com.deledzis.messenger.di.component

import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.integration.*
import com.deledzis.messenger.presentation.di.builder.MainActivityBuilder
import com.deledzis.messenger.presentation.di.module.ViewModelModule
//import com.deledzis.messenger.ui.LoginFailRegisterTest
import com.deledzis.messenger.ui.LoginSuccessLogoutTest
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestAppModule::class,
        TestNetworkModule::class,
        UtilsModule::class,
        TestRepositoriesModule::class,
        TestCacheModule::class,
        MainActivityBuilder::class,
        ViewModelModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class
    ]
)
interface TestAppComponent {

    fun into(test: AuthRepositoryIntegrationTest)
    fun into(test: ChatsRepositoryIntegrationTest)
    fun into(test: MessagesRepositoryIntegrationTest)
    fun into(test: UsersRepositoryIntegrationTest)
    fun into(test: RepositoriesIntegrationTest)
    fun into(test: LoginSuccessLogoutTest)
//    fun into(test: LoginFailRegisterTest)

}