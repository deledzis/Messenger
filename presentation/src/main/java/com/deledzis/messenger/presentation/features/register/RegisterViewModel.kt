package com.deledzis.messenger.presentation.features.register

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.extensions.orZero
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.model.request.auth.RegisterRequest
import com.deledzis.messenger.domain.model.response.auth.RegisterResponse
import com.deledzis.messenger.domain.usecase.auth.RegisterUseCase
import com.deledzis.messenger.infrastructure.util.SingleEventLiveData
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject

class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    userData: BaseUserData
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(registerUseCase.receiveChannel)

    val username = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    val user = MutableLiveData<Auth>(userData.getAuthUser())
    val usernameError = SingleEventLiveData<Int>()
    val nicknameError = SingleEventLiveData<Int>()
    val passwordError = SingleEventLiveData<Int>()
    val registerError = SingleEventLiveData<Int>()

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
            is RegisterResponse -> handleRegisterResponse(data)
        }
    }

    override fun handleFailure(error: Error) {
        super.handleFailure(error)
        error.exception?.asHttpError?.let {
            when {
                it.isGeneralError -> registerError.postValue(R.string.error_api_400)
                it.isMissingLoginError -> registerError.postValue(R.string.error_api_401)
                it.isMissingPasswordError -> registerError.postValue(R.string.error_api_402)
                it.isUserAlreadyExistsError -> registerError.postValue(R.string.error_api_405)
                else -> Unit
            }
        }
    }

    private fun clearErrors() {
        usernameError.postValue(0)
        nicknameError.postValue(0)
        passwordError.postValue(0)
        registerError.postValue(0)
    }

    fun register() {
        clearErrors()
        startLoading()

        val username = username.value
        val nickname = nickname.value
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
        if (nickname?.length.orZero() > 100) {
            nicknameError.postValue(R.string.error_nickname_invalid_length)
            stopLoading()
            return
        }

        registerUseCase(RegisterRequest(username, nickname, password))
    }

    private fun handleRegisterResponse(data: RegisterResponse) {
        if (data.response.accessToken.isNotBlank()) {
            handleRegisterOkResponse(data.response)
        } else {
            registerError.postValue(R.string.error_register_failed)
        }
        stopLoading()
    }

    private fun handleRegisterOkResponse(auth: Auth) {
        user.value = auth
    }
}