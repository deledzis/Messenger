package com.deledzis.messenger.presentation.features.login

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.model.request.auth.LoginRequest
import com.deledzis.messenger.domain.model.response.auth.LoginResponse
import com.deledzis.messenger.domain.usecase.auth.LoginUseCase
import com.deledzis.messenger.infrastructure.util.SingleEventLiveData
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    userData: BaseUserData
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(loginUseCase.receiveChannel)

    var username = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    val user: MutableLiveData<Auth> = MutableLiveData<Auth>(userData.getAuthUser())
    val usernameError = SingleEventLiveData<Int>()
    val passwordError = SingleEventLiveData<Int>()
    val loginError = SingleEventLiveData<Int>()

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
                it.isGeneralError -> loginError.postValue(R.string.error_api_400)
                it.isMissingLoginError -> loginError.postValue(R.string.error_api_401)
                it.isMissingPasswordError -> loginError.postValue(R.string.error_api_402)
                it.isWrongCredentialsError -> loginError.postValue(R.string.error_api_403)
                else -> Unit
            }
        }
    }

    private fun clearErrors() {
        usernameError.postValue(0)
        passwordError.postValue(0)
        loginError.postValue(0)
    }

    fun login() {
        clearErrors()
        startLoading()

        val username = username.value
        val password = password.value

        if (username.isNullOrBlank()) {
            usernameError.postValue(R.string.error_empty_username)
            stopLoading()
            return
        }
        if (password.isNullOrBlank()) {
            passwordError.postValue(R.string.error_password_invalid_length)
            stopLoading()
            return
        }
        if (username.length !in (4..100)) {
            usernameError.postValue(R.string.error_username_invalid_length)
            stopLoading()
            return
        }

        loginUseCase(LoginRequest(username, password))
    }

    private fun handleLoginResponse(data: LoginResponse) {
        if (data.response.accessToken.isNotBlank()) {
            handleLoginOkResponse(data.response)
        } else {
            loginError.postValue(R.string.error_auth_failed)
        }
        stopLoading()
    }

    private fun handleLoginOkResponse(auth: Auth) {
        user.value = auth
    }
}