package com.deledzis.messenger

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.di.CacheModule
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.di.RepositoriesModule
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.repository.AuthRepository
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.remote.di.NetworkModule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

class AuthRepositoryIntegrationTest {

    @Inject
    lateinit var repository: AuthRepository

    @Inject
    lateinit var userData: BaseUserData

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
        val component = DaggerTestAppComponent.builder()
            .cacheModule(CacheModule())
            .networkModule(NetworkModule())
            .repositoriesModule(RepositoriesModule())
            .utilsModule(UtilsModule())
            .testAppModule(TestAppModule(context))
            .build()
        component.into(this)
    }

    @After
    fun tearDown() {
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

    @Test
    fun register() {
        runBlocking {
            val result = repository.register(
                username = "username2",
                nickname = "nickname2",
                password = "password2"
            )
            assertThat(result is Response.Success).isTrue()
            val data = (result as Response.Success).successData.response
            assertThat(data.username).isEqualTo("username2")
            assertThat(data.nickname).isEqualTo("nickname2")
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