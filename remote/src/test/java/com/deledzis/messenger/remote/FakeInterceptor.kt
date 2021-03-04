package com.deledzis.messenger.remote

import com.deledzis.messenger.data.model.auth.AuthEntity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class FakeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val fakeMessage: String = toJson(
            AuthEntity(
                id = 1,
                username = "Igor",
                nickname = "Igorek",
                accessToken = "123"
            )
        )

        val original = chain.request()

        return Response.Builder()
            .code(200)
            .message(fakeMessage)
            .request(original)
            .protocol(Protocol.HTTP_1_0)
            .body(fakeMessage.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
            .build()
    }
}

inline fun <reified T> toJson(value: T): String {
    return Gson().toJson(value, object : TypeToken<T>() {}.type)
}

inline fun <reified T> fromJson(json: String): T {
    return Gson().fromJson(json, object : TypeToken<T>() {}.type)
}