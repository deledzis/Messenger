package com.deledzis.messenger.presentation.base

import com.deledzis.messenger.old.App
import com.deledzis.messenger.old.data.remote.NetworkManager
import com.deledzis.messenger.old.util.extensions.tag
import com.deledzis.messenger.old.util.logd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.net.ConnectException

open class BaseRepository {
    private val networkManager = NetworkManager(App.injector.context())

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): T? {
        val result: Result<T> = safeApiResult(call)
        var data: T? = null
        when (result) {
            is Result.Success -> data = result.data
            is Result.Error -> {
                logd(this.tag(), "Exception - ${result.exception}")
                if (result.exception is AppException) {
                    withContext(Dispatchers.Main) {
                        App.injector.context().toast(result.exception.message)
                    }
                }
            }
        }
        return data
    }

    private suspend fun <T : Any> safeApiResult(
        call: suspend () -> Response<T>
    ): Result<T> {
        if (networkManager.isConnectedToInternet) {
            try {
                val response = call.invoke()

                if (response.isSuccessful) {
                    return Result.Success(response.body()!!)
                }

                return Result.Error(ApiException(message = getMessageForCode(response.code())))
            } catch (e: ConnectException) {
                return Result.Error(ServerDownException())
            }
        } else {
            return Result.Error(NoInternetConnectionException())
        }
    }
}

fun getMessageForCode(apiCode: Int): String = when (apiCode) {
    400 -> "Не удалось выполнить запрос"
    401 -> "Не указан логин"
    402 -> "Не указан пароль"
    403 -> "Неправильный логин или пароль"
    404 -> "Неправильный логин или пароль"
    405 -> "Пользователь с таким логином уже зарегистрирован"
    406 -> "Ошибка авторизации. Пожалуйста, авторизуйтесь заново"
    407 -> "Собеседник не найден"
    408 -> "Чат не найден"
    409 -> "Не указан пользователь"
    410 -> "Не указан собеседник"
    411 -> "Не указан чат"
    412 -> "Не удалось отправить сообщение"
    413 -> "Не указан текущий пароль"
    414 -> "Указан неверный текущий пароль"
    415 -> "Не удалось обновить пользователя"
    416 -> "Диалог уже создан"
    else -> "Не удалось выполнить запрос: неизвестная ошибка сервера"
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

sealed class AppException(override val message: String) : Exception(message)

class ServerDownException : AppException("Сервер не отвечает. Пожалуйста, попробуйте позднее")
class ApiException(override val message: String) : AppException(message)
class NoInternetConnectionException : AppException("Отсутствует подключение к интернету")