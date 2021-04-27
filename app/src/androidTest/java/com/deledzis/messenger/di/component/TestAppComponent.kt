package com.deledzis.messenger.di.component

import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.integration.AuthRepositoryIntegrationTest
import com.deledzis.messenger.integration.ChatsRepositoryIntegrationTest
import com.deledzis.messenger.integration.MessagesRepositoryIntegrationTest
import com.deledzis.messenger.integration.UsersRepositoryIntegrationTest
import com.deledzis.messenger.presentation.di.builder.MainActivityBuilder
import com.deledzis.messenger.presentation.di.module.ViewModelModule
import com.deledzis.messenger.ui.LoginFailThenSuccessTest
import com.deledzis.messenger.ui.LoginSuccessLogoutTest
import com.deledzis.messenger.ui.RegisterFailLoginLogoutTest
import com.deledzis.messenger.ui.RegisterSuccessDeleteAccountTest
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
    fun into(test: LoginSuccessLogoutTest)
    fun into(test: LoginFailThenSuccessTest)
    fun into(test: RegisterFailLoginLogoutTest)
    fun into(test: RegisterSuccessDeleteAccountTest)

}