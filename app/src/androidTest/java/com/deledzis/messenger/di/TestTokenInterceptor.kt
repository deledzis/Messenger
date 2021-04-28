package com.deledzis.messenger.di

import com.deledzis.messenger.cache.preferences.user.UserData
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TestTokenInterceptor @Inject constructor(private val userData: UserData) : Interceptor {

    init {
        Timber.e("Initializing token interceptor")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // for auth
        if (
            (original.url.encodedPath.contains("/users/login")
                    || original.url.encodedPath.contains("/users/register"))
            && original.method.toUpperCase(Locale.getDefault()) == "POST"
        ) {
            return chain.proceed(original)
        }

        val originalHttpUrl = original.url
        val requestBuilder = original.newBuilder()
        userData.getAuthUser()?.let { user ->
            requestBuilder.addHeader("Authorization", "Bearer ${user.accessToken}")
        }
        requestBuilder.url(originalHttpUrl)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }

}