package com.deledzis.messenger.base

import com.deledzis.messenger.App
import com.deledzis.messenger.data.remote.NetworkManager
import com.deledzis.messenger.util.extensions.tag
import com.deledzis.messenger.util.logd
import com.deledzis.messenger.util.loge
import retrofit2.Response
import java.io.IOException

open class BaseRepository {
    val netManager = NetworkManager(App.injector.context())

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): T? {
        val result: Result<T> = safeApiResult(call, errorMessage)
        var data: T? = null
        when (result) {
            is Result.Success -> data = result.data
            is Result.Error -> {
                logd(this.tag(), "$errorMessage & Exception - ${result.exception}")
                if (result.exception is NoInternetConnectionException) {
                    throw NoInternetConnectionException()
                }
                if (result.exception is TestTimeBetweenAttemptsNotGoneException) {
                    throw TestTimeBetweenAttemptsNotGoneException()
                }
            }
        }
        return data
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        if (netManager.isConnectedToInternet) {
            val response = call.invoke()

            if (response.isSuccessful) {
                return Result.Success(response.body()!!)
            }
            loge(tag(), "Raw response: ${response.raw()}")
            loge(tag(), "Error message: $errorMessage")

            if (response.code() == 400 && response.raw().request.url.encodedPath.contains("tests/attempts")) {
                return Result.Error(TestTimeBetweenAttemptsNotGoneException())
            }

            return Result.Error(IOException("Error Occurred during getting safe Api result, Custom ERROR - $errorMessage"))
        } else {
            return Result.Error(NoInternetConnectionException())
        }
    }

    class NoInternetConnectionException : Exception()
    class TestTimeBetweenAttemptsNotGoneException : Exception() {
        override val message: String?
            get() = "Время между попытками еще не прошло"
    }
}

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out T : Any> {

    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}