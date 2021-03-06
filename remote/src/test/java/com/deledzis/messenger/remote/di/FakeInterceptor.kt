package com.deledzis.messenger.remote.di

import com.deledzis.messenger.remote.RemoteTestData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.*

class FakeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var fakeMessage = ""

        val original = chain.request()

        when {
            (original.url.encodedPath.contains("/users/login")
                    || original.url.encodedPath.contains("/users/register"))
                    && original.method.toUpperCase(Locale.getDefault()) == "POST"
                    || original.url.encodedPath.contains("/users")
                    && original.method.toUpperCase(Locale.getDefault()) == "PUT" -> {
                fakeMessage = toJson(RemoteTestData.auth)
            }

            Regex("/chats$").containsMatchIn(original.url.encodedPath)
                    && original.method.toUpperCase(Locale.getDefault()) == "GET" -> {
                fakeMessage = toJson(RemoteTestData.chats)
            }

            original.url.encodedPath.contains("/chats")
                    && original.method.toUpperCase(Locale.getDefault()) == "POST" -> {
                fakeMessage = toJson(RemoteTestData.chat)
            }

            Regex("/chats/\\d+").containsMatchIn(original.url.encodedPath)
                    && original.method.toUpperCase(Locale.getDefault()) == "POST" -> {
                fakeMessage = toJson(RemoteTestData.serverMessageResponse)
            }

            Regex("/chats/\\d+").containsMatchIn(original.url.encodedPath)
                    && original.method.toUpperCase(Locale.getDefault()) == "GET" -> {
                fakeMessage = toJson(RemoteTestData.messages)
            }

            Regex("/users/\\d+").containsMatchIn(original.url.encodedPath)
                    && original.method.toUpperCase(Locale.getDefault()) == "GET" -> {
                fakeMessage = toJson(RemoteTestData.user)
            }

            original.url.encodedPath.contains("/users")
                    && original.method.toUpperCase(Locale.getDefault()) == "GET" -> {
                fakeMessage = toJson(RemoteTestData.users)
            }

        }


        val response = Response.Builder()
            .code(200)
            .message(fakeMessage)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_0)
            .body(fakeMessage.toByteArray().toResponseBody("application/json".toMediaTypeOrNull()))
            .build()


        return response
    }
}

inline fun <reified T> toJson(value: T): String {
    return Gson().toJson(value, object : TypeToken<T>() {}.type)
}

inline fun <reified T> fromJson(json: String): T {
    return Gson().fromJson(json, object : TypeToken<T>() {}.type)
}