package com.deledzis.messenger.di.model

import com.deledzis.messenger.di.scopes.ApplicationScope
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject

@ApplicationScope
class TokenInterceptor @Inject constructor() : Interceptor {
    var token: String = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // for auth
        if (original.url.encodedPath.contains("/auth") &&
            original.method.toUpperCase(Locale.getDefault()) == "POST"
        ) {
            return chain.proceed(original)
        }

        val originalHttpUrl = original.url
        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "JWT $token")
            .url(originalHttpUrl)

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}