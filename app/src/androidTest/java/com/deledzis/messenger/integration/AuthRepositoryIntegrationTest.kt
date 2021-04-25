package com.deledzis.messenger.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.remote.ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class AuthRepositoryIntegrationTest {

    @Inject
    lateinit var repository: AuthRepositoryImpl

    @Inject
    lateinit var userData: UserData

    @Inject
    lateinit var api: ApiService

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
    fun login() {
        runBlocking {
            val result = repository.login(
                username = "username",
                password = "password"
            )
            assertThat(result is Response.Success).isTrue()
            val data = (result as Response.Success).successData.response
            assertThat(data.username).isEqualTo("username")
            assertThat(data.nickname).isEqualTo("nickname")
        }
    }

    /*@Test
    fun loginAndDelete() {
        runBlocking {
            val loginResult = repository.login(
                username = "test",
                password = "testtest"
            )
            assertThat(loginResult is Response.Success).isTrue()
            val data = (loginResult as Response.Success).successData.response
            assertThat(data.username).isEqualTo("test")
            userData.saveAuthUser(data)

            val deleteResult = repository.deleteAccount(username = "")
            assertThat(deleteResult is Response.Success).isTrue()
            assertThat((deleteResult as Response.Success).successData.response.errorCode).isEqualTo(0)
            assertThat(deleteResult.successData.response.message).isEqualTo("deleted")
            userData.saveAuthUser(null)
        }
    }*/

    @Test
    fun register() {
        runBlocking {
            val registerResult = repository.register(
                username = "test",
                nickname = null,
                password = "testtest"
            )
            assertThat(registerResult is Response.Success).isTrue()
            val data = (registerResult as Response.Success).successData.response
            assertThat(data.username).isEqualTo("test")
            userData.saveAuthUser(data)

            val deleteResult = repository.deleteAccount(username = "")
            assertThat(deleteResult is Response.Success).isTrue()
            assertThat((deleteResult as Response.Success).successData.response.errorCode).isEqualTo(
                0
            )
            assertThat(deleteResult.successData.response.message).isEqualTo("deleted")
            userData.saveAuthUser(null)
        }
    }

    @Test
    fun updateUserData() {
        runBlocking {
            userData.saveAuthUser(null)
            val result = repository.login(
                username = "username",
                password = "password"
            )
            assertThat(result is Response.Success).isTrue()
            val id = (result as Response.Success).successData.response.id
            val accessToken = result.successData.response.accessToken
            userData.saveAuthUser(Auth(id, "username", "password", accessToken))
            val result1 = repository.updateUserData(
                username = "username",
                nickname = "nickname",
                password = "password",
                newPassword = "newpassword"
            )
            assertThat(result1 is Response.Success).isTrue()
            val result2 = repository.updateUserData(
                username = "username",
                nickname = "nickname",
                password = "newpassword",
                newPassword = "password"
            )
            assertThat(result2 is Response.Success).isTrue()
            val data = (result2 as Response.Success).successData.response
            assertThat(data.username).isEqualTo("username")
            assertThat(data.nickname).isEqualTo("nickname")
            val result3 = repository.updateUserData(
                username = "username1",
                nickname = "nickname1",
                password = "newpassword1",
                newPassword = "password1"
            )
            assertThat(result3 is Response.Failure).isTrue()
            userData.saveAuthUser(null)
        }
    }
}