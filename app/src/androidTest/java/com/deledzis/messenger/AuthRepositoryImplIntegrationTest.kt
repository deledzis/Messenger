package com.deledzis.messenger

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.deledzis.messenger.cache.di.CacheModule
import com.deledzis.messenger.cache.mapper.UserMapper
import com.deledzis.messenger.cache.preferences.user.UserStore
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.data.di.TokenInterceptor
import com.deledzis.messenger.data.mapper.auth.AuthMapper
import com.deledzis.messenger.data.repository.auth.AuthRepositoryImpl
import com.deledzis.messenger.data.source.auth.AuthDataStoreFactory
import com.deledzis.messenger.data.source.auth.AuthRemoteDataStore
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import com.deledzis.messenger.infrastructure.services.NetworkManager
import com.deledzis.messenger.remote.di.NetworkModule
import kotlinx.coroutines.runBlocking


class AuthRepositoryIntegrationTest {
    companion object {
        private const val REQUEST_TIMEOUT = 60
        private const val PREF_APP_PACKAGE_NAME = "com.deledzis.messenger.testpreferences"
    }

    lateinit var repository: AuthRepositoryImpl
    lateinit var userDataImpl : BaseUserData

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        val sharedPreferences = context.getSharedPreferences(PREF_APP_PACKAGE_NAME, Context.MODE_PRIVATE)
        val userStore = UserStore(sharedPreferences)
        userDataImpl = CacheModule().provideUserStore(userStore, UserMapper())
        val networkModule = NetworkModule()
        val gson = networkModule.provideGson()
        val interceptor = networkModule.provideRequestHeadersInterceptor()
        val httpLoggingInterceptor = networkModule.provideHttpLoggingInterceptor()
        val tokenInterceptor = TokenInterceptor(userDataImpl)
        val okHttpClientBuilder = networkModule.provideOkHttpClient(httpLoggingInterceptor, interceptor, tokenInterceptor)
        val retrofit = networkModule.provideRetrofit(gson, okHttpClientBuilder)
        val api = networkModule.provideApiInterface(retrofit)
        val authRemote = networkModule.provideAuthRemote(api)
        val remoteDataStore = AuthRemoteDataStore(authRemote)
        val networkManager = NetworkManager(context)
        val authMapper = AuthMapper()
        val authFactory = AuthDataStoreFactory(remoteDataStore)
        repository = AuthRepositoryImpl(authFactory, authMapper, networkManager)
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
            userDataImpl.saveAuthUser(null)
            val result = repository.login(
                username = "username",
                password = "password"
            )
            assertThat(result is Response.Success).isTrue()
            val id = (result as Response.Success).successData.response.id
            val accessToken = (result as Response.Success).successData.response.accessToken
            userDataImpl.saveAuthUser(Auth(id, "username", "password", accessToken))
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
            userDataImpl.saveAuthUser(null)
        }
    }
}