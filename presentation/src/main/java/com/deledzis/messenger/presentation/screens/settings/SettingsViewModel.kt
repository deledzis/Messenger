package com.deledzis.messenger.presentation.screens.settings

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.usecase.auth.UpdateUserDataUseCase
import com.deledzis.messenger.presentation.base.BaseViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    userData: BaseUserData
) : BaseViewModel() {

    val userData = MutableLiveData<Auth>()

    var username = MutableLiveData(userData.getAuthUser()?.username)
    val usernameError = MutableLiveData<Int>()

    var nickname = MutableLiveData(userData.getAuthUser()?.nickname)
    val nicknameError = MutableLiveData<Int>()

    var password = MutableLiveData<String>(null)
    val passwordError = MutableLiveData<Int>()

    var newPassword = MutableLiveData<String>(null)
    val newPasswordError = MutableLiveData<Int>()

    private fun clearErrors() {
        usernameError.value = null
        nicknameError.value = null
        passwordError.value = null
        newPasswordError.value = null
    }

    fun updateUserData() {
        clearErrors()
        scope.launch {
            startLoading()
            // TODO -- add an extension function-validator for string LiveDatas
            //  that will throw an extension if condition is failed
            //  so that we can wrap this launch in a try-catch block
            //  and do stopLoading() in a catch section instead of doing it every time
            if (username.value.isNullOrBlank()) {
                usernameError.postValue("Нужно ввести логин")
                stopLoading()
                return@launch
            }
            if (password.value.isNullOrBlank()) {
                passwordError.postValue("Нужно ввести действующий пароль")
                stopLoading()
                return@launch
            }
            if (username.value?.length !in (4..100)) {
                usernameError.postValue("Логин должен содержать от 4 до 100 символов включительно")
                stopLoading()
                return@launch
            }
            if (nickname.value?.length !in (1..100)) {
                nicknameError.postValue("Имя может содержать до 100 символов включительно")
                stopLoading()
                return@launch
            }
            if (newPassword.value != null && newPassword.value?.length !in (8..64)) {
                newPasswordError.postValue("Пароль должен содержать от 8 до 64 символов включительно")
                stopLoading()
                return@launch
            }
            val response = repository.updateUserData(
                id = App.injector.userData().authorizedUser.id ?: 0,
                username = username.value!!,
                nickname = nickname.value,
                password = password.value,
                newPassword = newPassword.value
            )
            if (response == null) {
                error.postValue("Не удалось изменить данные аккаунта")
            } else {
                userData.postValue(response)
            }
            stopLoading()
        }
    }
}
