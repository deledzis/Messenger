package com.deledzis.messenger

import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.common.Constants
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.di.TokenInterceptor
import com.deledzis.messenger.di.component.DaggerApplicationComponent
import com.deledzis.messenger.domain.model.BaseNetworkManager
import com.deledzis.messenger.domain.repository.AuthRepository
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class AuthRepositoryIntegrationTest {
    companion object {
        private const val REQUEST_TIMEOUT = 60
    }

    private val testDispatcher = TestCoroutineDispatcher()
    @Inject
    lateinit var repository: AuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val app : App = InstrumentationRegistry.getInstrumentation().newApplication() as App
        DaggerApplicationComponent.factory().create(app)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun login() {
        runBlockingTest {
//            Mockito.`when`(repository.login(Mockito.anyString(), Mockito.anyString())).then {
//                return@then Response.Success(
//                    LoginResponse(
//                        Auth(
//                            id = 0,
//                            username = "username",
//                            nickname = "nickname",
//                            accessToken = "accessToken"
//                        )
//                    )
//                )
//            }
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
        runBlockingTest {
//            repository = Mockito.mock(AuthRepository::class.java)
//            Mockito.`when`(
//                repository.register(
//                    Mockito.anyString(),
//                    Mockito.anyString(),
//                    Mockito.anyString()
//                )
//            ).then {
//                return@then Response.Success(
//                    RegisterResponse(
//                        Auth(
//                            id = 0,
//                            username = "username",
//                            nickname = "nickname",
//                            accessToken = "accessToken"
//                        )
//                    )
//                )
//            }
            val result = repository.register(
                username = "username",
                nickname = "nickname",
                password = "password"
            )
            assertThat(result is Response.Success).isTrue()
            val data = (result as Response.Success).successData.response
            assertThat(data.username).isEqualTo("username")
            assertThat(data.nickname).isEqualTo("nickname")
        }
    }

    @Test
    fun updateUserData() {
        runBlockingTest {
//            repository = Mockito.mock(AuthRepository::class.java)
//            Mockito.`when`(
//                repository.updateUserData(
//                    username = "username",
//                    nickname = "nickname",
//                    password = "password",
//                    newPassword = "newpassword"
//                )
//            ).then {
//                return@then Response.Failure(Error.NetworkConnectionError())
//            }
            val result = repository.updateUserData(
                username = "username",
                nickname = "nickname",
                password = "password",
                newPassword = "newpassword"
            )
            assertThat(result is Response.Failure).isTrue()

//            Mockito.`when`(
//                repository.updateUserData(
//                    username = "username1",
//                    nickname = "nickname1",
//                    password = "password1",
//                    newPassword = "newpassword1"
//                )
//            ).then {
//                return@then Response.Success(
//                    UpdateUserDataResponse(
//                        Auth(
//                            id = 0,
//                            username = "username1",
//                            nickname = "nickname1",
//                            accessToken = "newAccessToken"
//                        )
//                    )
//                )
//            }
            val result2 = repository.updateUserData(
                username = "username1",
                nickname = "nickname1",
                password = "password1",
                newPassword = "newpassword1"
            )
            assertThat(result2 is Response.Success).isTrue()
            val data = (result2 as Response.Success).successData.response
            assertThat(data.username).isEqualTo("username1")
            assertThat(data.nickname).isEqualTo("nickname1")
            assertThat(data.accessToken).isEqualTo("newAccessToken")
        }
    }
}