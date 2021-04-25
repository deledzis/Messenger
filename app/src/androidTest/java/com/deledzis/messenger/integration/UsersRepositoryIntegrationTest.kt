package com.deledzis.messenger.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.data.repository.chats.ChatsRepositoryImpl
import com.deledzis.messenger.data.repository.users.UsersRepositoryImpl
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class UsersRepositoryIntegrationTest {

    @Inject
    lateinit var authRepository: AuthRepositoryImpl

    @Inject
    lateinit var usersRepository: UsersRepositoryImpl

    @Inject
    lateinit var chatsRepository: ChatsRepositoryImpl

    @Inject
    lateinit var userData: UserData

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val component = DaggerTestAppComponent.builder()
            .testCacheModule(TestCacheModule())
            .testNetworkModule(TestNetworkModule())
            .testRepositoriesModule(TestRepositoriesModule())
            .utilsModule(UtilsModule())
            .testAppModule(TestAppModule(context))
            .build()
        component.into(this)
    }

    @Test
    fun getUser() {
        runBlocking {
            userData.saveAuthUser(null)
            val result = authRepository.login(
                username = "username",
                password = "password"
            )
            assertThat(result is Response.Success).isTrue()
            val id = (result as Response.Success).successData.response.id
            val accessToken = result.successData.response.accessToken
            userData.saveAuthUser(Auth(id, "username", "password", accessToken))

            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isNotNull()
            assertThat(data1.items).isNotEmpty()

            val result4 = usersRepository.getUsers("")
            assertThat(result4 is Response.Success).isTrue()
            val data4 = (result4 as Response.Success).successData.response
            assertThat(data4.items).isNotNull()
            assertThat(data4.items).isNotEmpty()

            val result2 = usersRepository.getUser(1)
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response
            assertThat(data2.id).isEqualTo(1)

            val result3 = usersRepository.getUser(999999)
            assertThat(result3 is Response.Failure).isTrue()
            userData.saveAuthUser(null)
        }
    }

    @Test
    fun getUsers() {
        runBlocking {
            userData.saveAuthUser(null)
            val result = authRepository.login(
                username = "username",
                password = "password"
            )
            assertThat(result is Response.Success).isTrue()
            val id = (result as Response.Success).successData.response.id
            val accessToken = result.successData.response.accessToken
            userData.saveAuthUser(Auth(id, "username", "password", accessToken))

            val result3 = chatsRepository.getChats()
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isNotNull()
            assertThat(data3.items).isNotEmpty()

            val result1 = usersRepository.getUsers("")
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isNotNull()
            assertThat(data1.items).isNotEmpty()

            val result2 = usersRepository.getUsers("For Testing Purposes")
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response
            assertThat(data2.items).isNotNull()
            assertThat(data2.items).isEmpty()
            userData.saveAuthUser(null)
        }
    }

}