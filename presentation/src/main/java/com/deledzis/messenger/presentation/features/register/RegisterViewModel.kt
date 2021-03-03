package com.deledzis.messenger.presentation.features.register

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.model.request.auth.RegisterRequest
import com.deledzis.messenger.domain.model.response.auth.RegisterResponse
import com.deledzis.messenger.domain.usecase.auth.RegisterUseCase
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
    val user: MutableLiveData<Auth> = MutableLiveData<Auth>(userData.getAuthUser())
    val registerError: MutableLiveData<Int> = MutableLiveData<Int>()

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
                it.isMissingLoginError -> registerError.value = R.string.error_api_401
                it.isMissingPasswordError -> registerError.value = R.string.error_api_402
                it.isUserAlreadyExistsError -> registerError.value = R.string.error_api_405
                else -> Unit
            }
        }
    }

    private fun clearErrors() {
        registerError.value = 0
    }

    fun register() {
        clearErrors()
        startLoading()

        registerUseCase(
            params = RegisterRequest(
                username = username.value ?: kotlin.run {
                    stopLoading()
                    return
                },
                nickname = nickname.value ?: kotlin.run {
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

    private fun handleRegisterResponse(data: RegisterResponse) {
        if (data.response.accessToken.isNotBlank()) {
            handleRegisterOkResponse(data.response)
        } else {
            registerError.value = R.string.error_register_failed
        }
        stopLoading()
    }

    private fun handleRegisterOkResponse(auth: Auth) {
        user.value = auth
    }
}