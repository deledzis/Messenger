package com.deledzis.messenger.util

import com.deledzis.messenger.BuildConfig
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

val isDebug = BuildConfig.DEBUG

inline fun <reified T> toJson(value: T): String {
    return Gson().toJson(value, object : TypeToken<T>() {}.type)
}

inline fun <reified T> fromJson(json: String): T {
    return Gson().fromJson(json, object : TypeToken<T>() {}.type)
}