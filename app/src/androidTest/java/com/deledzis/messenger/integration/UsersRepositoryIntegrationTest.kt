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
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import javax.inject.Inject

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
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

        runBlocking {
            userData.saveAuthUser(null)
            authRepository.register(
                username = "test",
                nickname = null,
                password = "testtest"
            )
            authRepository.login(
                username = "test",
                password = "testtest"
            ).handleResult {
                userData.saveAuthUser(it.response)
            }
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            authRepository.login("test", "testtest").handleResult {
                userData.saveAuthUser(it.response)
                authRepository.deleteAccount(username = "test")
            }
            userData.saveAuthUser(null)
        }
    }

    @Test
    fun test1_getUser() {
        runBlocking {
            delay(1000)

            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()

            val result2 = usersRepository.getUsers(search = "sasha")
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response
            assertThat(data2.items).isNotEmpty()
            assertThat(data2.items[0].id).isNotNull()

            val result3 = usersRepository.getUser(id = data2.items[0].id!!)
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.id).isGreaterThan(0)

            val result4 = usersRepository.getUser(-999999)
            assertThat(result4 is Response.Failure).isTrue()
        }
    }

    @Test
    fun test2_getUsers() {
        runBlocking {
            delay(1000)

            val result1 = chatsRepository.getChats()
            assertThat(result1 is Response.Success).isTrue()
            val data1 = (result1 as Response.Success).successData.response
            assertThat(data1.items).isEmpty()

            val result2 = usersRepository.getUsers(search = "test")
            assertThat(result2 is Response.Success).isTrue()
            val data2 = (result2 as Response.Success).successData.response
            assertThat(data2.items).isNotEmpty()
            assertThat(data2.items[0].id).isNotNull()

            val result3 = usersRepository.getUsers(search = "fasvdvfdvfd")
            assertThat(result3 is Response.Success).isTrue()
            val data3 = (result3 as Response.Success).successData.response
            assertThat(data3.items).isEmpty()
        }
    }

}