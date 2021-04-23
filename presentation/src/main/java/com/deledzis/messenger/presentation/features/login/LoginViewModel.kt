package com.deledzis.messenger.presentation.features.login

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.OpenForTesting
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.model.request.auth.LoginRequest
import com.deledzis.messenger.domain.model.response.auth.LoginResponse
import com.deledzis.messenger.domain.usecase.auth.LoginUseCase
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject

@OpenForTesting
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    userData: BaseUserData
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(loginUseCase.receiveChannel)

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    val user: MutableLiveData<Auth> = MutableLiveData<Auth>(userData.getAuthUser())
    val loginError: MutableLiveData<Int> = MutableLiveData<Int>()

    override suspend fun resolve(value: Response<Entity, Error>) {
        value.handleResult(
            stateBlock = ::handleState,
            failureBlock = ::handleFailure,
            successBlock = ::handleSuccess
        )
    }

    private fun handleSuccess(data: Any?) {
        Timber.i("Handle Success: $data")
        when (data) {
            is LoginResponse -> handleLoginResponse(data)
        }
    }

    override fun handleFailure(error: Error) {
        super.handleFailure(error)
        error.exception?.asHttpError?.let {
            when {
                it.isMissingLoginError -> loginError.value = R.string.error_api_401
                it.isMissingPasswordError -> loginError.value = R.string.error_api_402
                it.isWrongCredentialsError -> loginError.value = R.string.error_api_403
                else -> Unit
            }
        }
    }

    private fun clearErrors() {
        loginError.value = 0
    }

    fun login() {
        clearErrors()
        startLoading()

        loginUseCase(
            params = LoginRequest(
                username = username.value ?: kotlin.run {
                    stopLoading()
                    return
                },
                password = password.value ?: kotlin.run {
                    stopLoading()
                    return
                }
            )
        )
    }

    private fun handleLoginResponse(data: LoginResponse) {
        if (!data.response.accessToken.isNullOrBlank()) {
            handleLoginOkResponse(data.response)
        } else {
            /*logError(
                errorCode = data.response.errorCode,
                originMessage = data.response.message,
                normalizedMessage = "Не удалось войти с предоставленными учетными данными"
            )*/
            loginError.value = R.string.error_auth_failed
        }
        stopLoading()
    }

    private fun handleLoginOkResponse(auth: Auth) {
        user.value = auth
    }
}