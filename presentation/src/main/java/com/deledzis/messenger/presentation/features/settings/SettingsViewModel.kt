package com.deledzis.messenger.presentation.features.settings

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.model.request.auth.UpdateUserDataRequest
import com.deledzis.messenger.domain.model.response.auth.UpdateUserDataResponse
import com.deledzis.messenger.domain.usecase.auth.UpdateUserDataUseCase
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    userData: BaseUserData
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(updateUserDataUseCase.receiveChannel)

    var username = MutableLiveData(userData.getAuthUser()?.username)
    val usernameError = MutableLiveData<Int>()

    var nickname = MutableLiveData(userData.getAuthUser()?.nickname)
    val nicknameError = MutableLiveData<Int>()

    var password = MutableLiveData<String>(null)
    val passwordError = MutableLiveData<Int>()

    var newPassword = MutableLiveData<String>(null)
    val newPasswordError = MutableLiveData<Int>()

    val userData = MutableLiveData<Auth>()

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
            is UpdateUserDataResponse -> handleUpdateUserDataResponse(data)
        }
    }

    private fun clearErrors() {
        usernameError.value = 0
        nicknameError.value = 0
        passwordError.value = 0
        newPasswordError.value = 0
    }

    fun updateUserData() {
        clearErrors()
        startLoading()

        // TODO -- add an extension function-validator for string LiveDatas
        //  that will throw an extension if condition is failed
        //  so that we can wrap this launch in a try-catch block
        //  and do stopLoading() in a catch section instead of doing it every time
        if (username.value.isNullOrBlank()) {
            usernameError.value = R.string.error_empty_username
            stopLoading()
            return
        }
        if (password.value.isNullOrBlank()) {
            passwordError.value = R.string.error_empty_actual_password
            stopLoading()
            return
        }
        if (username.value?.length !in (4..100)) {
            usernameError.value = R.string.error_username_invalid_length
            stopLoading()
            return
        }
        if (nickname.value?.length !in (1..100)) {
            nicknameError.value = R.string.error_nickname_invalid_length
            stopLoading()
            return
        }
        if (newPassword.value != null && newPassword.value?.length !in (8..64)) {
            newPasswordError.value = R.string.error_password_invalid_length
            stopLoading()
            return
        }

        updateUserDataUseCase(
            params = UpdateUserDataRequest(
                username = username.value ?: return,
                nickname = nickname.value,
                password = password.value,
                newPassword = newPassword.value
            )
        )
    }

    private fun handleUpdateUserDataResponse(data: UpdateUserDataResponse) {
        if (!data.response.accessToken.isNullOrBlank()) {
            handleUpdateUserDataOkResponse(data.response)
        }
        stopLoading()
    }

    private fun handleUpdateUserDataOkResponse(auth: Auth) {
        this.userData.value = auth
    }
}
