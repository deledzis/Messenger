package com.deledzis.messenger.data.di

import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(private val userData: BaseUserData) : Interceptor {
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