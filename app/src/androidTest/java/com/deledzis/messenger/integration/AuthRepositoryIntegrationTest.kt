package com.deledzis.messenger.integration

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.preferences.user.UserData
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.di.component.DaggerTestAppComponent
import com.deledzis.messenger.di.module.TestAppModule
import com.deledzis.messenger.di.module.TestCacheModule
import com.deledzis.messenger.di.module.TestNetworkModule
import com.deledzis.messenger.di.module.TestRepositoriesModule
import com.deledzis.messenger.domain.model.response.auth.DeleteAccountResponse
import com.deledzis.messenger.domain.model.response.auth.LoginResponse
import com.deledzis.messenger.domain.model.response.auth.RegisterResponse
import com.deledzis.messenger.domain.model.response.auth.UpdateUserDataResponse
import com.deledzis.messenger.infrastructure.di.UtilsModule
import com.deledzis.messenger.presentation.base.BaseViewModel.Companion.asHttpError
import com.deledzis.messenger.presentation.base.BaseViewModel.Companion.isServerUnavailableError
import com.deledzis.messenger.remote.ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import javax.inject.Inject

@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
class AuthRepositoryIntegrationTest {

    companion object {

        private const val MAX_RETRY_COUNT: Int = 5

    }

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

        runBlocking {
            userData.saveAuthUser(null)
            register(
                username = "test",
                nickname = null,
                password = "testtest"
            )
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            login("test_test", "testtest")?.handleResult {
                userData.saveAuthUser(it.response)
                repository.deleteAccount(username = "test_test")
            }
            login("test_test_test", "testtest")?.handleResult {
                userData.saveAuthUser(it.response)
                repository.deleteAccount(username = "test_test_test")
            }
            userData.saveAuthUser(null)
        }
    }

    @Test
    fun test1_login() {
        runBlocking {
            val result1 = login("test1", "testtest")
            assertThat(result1 is Response.Failure).isTrue()

            val result2 = login("test", "test_test")
            assertThat(result2 is Response.Failure).isTrue()

            val result3 = login("test", "testtest")
            assertThat(result3 is Response.Success).isTrue()
            val data = (result3 as Response.Success).successData.response
            assertThat(data.username).isEqualTo("test")
        }
    }

    @Test
    fun test2_register() {
        runBlocking {
            val result1 = register(username = "test", password = "testtest")
            assertThat(result1 is Response.Failure).isTrue()

            val result2 = register(username = "test_test", password = "testtest")
            assertThat(result2 is Response.Success).isTrue()
            val data = (result2 as Response.Success).successData.response
            assertThat(data.username).isEqualTo("test_test")
        }
    }

    @Test
    fun test3_updateUserData() {
        runBlocking {
            val result1 = login("test", "testtest")
            assertThat(result1 is Response.Success).isTrue()
            userData.saveAuthUser((result1 as Response.Success).successData.response)

            // change nickname, wrong password
            val result2 = updateUserData(
                username = "test",
                nickname = "nickname",
                password = "test_test",
                newPassword = null
            )
            assertThat(result2 is Response.Failure).isTrue()

            // change nickname, correct password
            val result3 = updateUserData(
                username = "test",
                nickname = "nickname",
                password = "testtest",
                newPassword = null
            )
            assertThat(result3 is Response.Success).isTrue()
            val data = (result3 as Response.Success).successData.response
            assertThat(data.username).isEqualTo("test")
            assertThat(data.nickname).isEqualTo("nickname")
            userData.saveAuthUser(result3.successData.response)

            // change password, wrong password
            val result4 = updateUserData(
                username = "test",
                nickname = "nickname",
                password = "password_wrong",
                newPassword = "password"
            )
            assertThat(result4 is Response.Failure).isTrue()

            // change password, correct password
            val result5 = updateUserData(
                username = "test",
                nickname = "nickname",
                password = "testtest",
                newPassword = "newpassword"
            )
            assertThat(result5 is Response.Success).isTrue()
            val data2 = (result5 as Response.Success).successData.response
            assertThat(data2.username).isEqualTo("test")
            assertThat(data2.nickname).isEqualTo("nickname")
            userData.saveAuthUser(result5.successData.response)

            // rollback
            val result6 = updateUserData(
                username = "test",
                nickname = null,
                password = "newpassword",
                newPassword = "testtest"
            )
            assertThat(result6 is Response.Success).isTrue()
            val data3 = (result6 as Response.Success).successData.response
            assertThat(data3.username).isEqualTo("test")
            assertThat(data3.nickname).isEqualTo(null)
        }
    }

    @Test
    fun test4_deleteAccount() {
        runBlocking {
            val result1 = register(
                username = "test_test_test",
                nickname = null,
                password = "testtest"
            )
            assertThat(result1 is Response.Success).isTrue()
            val data = (result1 as Response.Success).successData.response
            assertThat(data.username).isEqualTo("test_test_test")
            userData.saveAuthUser(data)

            val result2 = deleteAccount("test_test_test")
            assertThat(result2 is Response.Success).isTrue()
            userData.saveAuthUser(null)

            val result3 = login("test_test_test", "testtest")
            assertThat(result3 is Response.Failure).isTrue()
        }
    }

    private suspend fun login(
        username: String,
        password: String,
        count: Int = 1
    ): Response<LoginResponse, Error>? {
        var response: Response<LoginResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            repository.login(username, password).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        login(username, password, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun register(
        username: String,
        nickname: String? = null,
        password: String,
        count: Int = 1
    ): Response<RegisterResponse, Error>? {
        var response: Response<RegisterResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            repository.register(username, nickname, password).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        register(username, nickname, password, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun updateUserData(
        username: String,
        nickname: String? = null,
        password: String?,
        newPassword: String? = null,
        count: Int = 1
    ): Response<UpdateUserDataResponse, Error>? {
        var response: Response<UpdateUserDataResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            repository.updateUserData(username, nickname, password, newPassword).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        updateUserData(username, nickname, password, newPassword, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }

    private suspend fun deleteAccount(
        username: String,
        count: Int = 1
    ): Response<DeleteAccountResponse, Error>? {
        var response: Response<DeleteAccountResponse, Error>? = null
        if (count <= MAX_RETRY_COUNT) {
            repository.deleteAccount(username).handleResult(
                failureBlock = {
                    if (it.exception?.asHttpError?.isServerUnavailableError == true) {
                        deleteAccount(username, count + 1)
                    } else {
                        response = Response.Failure(it)
                    }
                },
                successBlock = { response = Response.Success(it) }
            )
        }
        return response
    }
}