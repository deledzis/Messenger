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
import com.deledzis.messenger.ui.*
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
    fun into(test: Test2_LoginSuccessLogoutTest)
    fun into(test: Test1_LoginFailThenSuccessTest)
    fun into(test: Test3_RegisterFailLoginLogoutTest)
    fun into(test: Test4_RegisterSuccessDeleteAccountTest)
    fun into(test: Test5_GetChatsOpenChatSendMessageTest)
    fun into(test: Test6_OpenChatSearchMessagesSuccessTest)
    fun into(test: Test7_OpenChatSearchMessagesFailTest)
    fun into(test: Test8_SettingsUpdateUsernameNicknameSuccessTest)
    fun into(test: Test9_SettingsUpdatePasswordFailTest)
    fun into(test: Test10_SettingsUpdatePasswordSuccessTest)
    fun into(test: Test11_AddChatAndSendMessageTest)
    fun into(test: Test12_OpenChatDeleteMessageTest)
    fun into(test: Test13_AddChatDifferentScenariosTest)

}