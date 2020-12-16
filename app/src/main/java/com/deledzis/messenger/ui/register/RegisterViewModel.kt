package com.deledzis.messenger.ui.register

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.AuthorizedUser
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {
    override val repository: RegisterRepository = RegisterRepository(App.injector.api())

    val userData = MutableLiveData<AuthorizedUser>()

    var username = MutableLiveData<String>()
    val usernameError = MutableLiveData<String>()

    var nickname = MutableLiveData<String>()
    val nicknameError = MutableLiveData<String>()

    var password = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    private fun clearErrors() {
        usernameError.postValue(null)
        nicknameError.postValue(null)
        passwordError.postValue(null)
    }

    fun register() {
        clearErrors()
        scope.launch {
            startLoading()
            if (username.value.isNullOrBlank()) {
                usernameError.postValue("Нужно ввести логин")
                stopLoading()
                return@launch
            }
            if (password.value.isNullOrBlank()) {
                passwordError.postValue("Нужно ввести пароль")
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
            if (password.value != null && password.value?.length !in (8..64)) {
                passwordError.postValue("Пароль должен содержать от 8 до 64 символов включительно")
                stopLoading()
                return@launch
            }
            val response = repository.register(
                username = username.value!!,
                nickname = nickname.value,
                password = password.value!!
            )
            if (response != null) {
                userData.postValue(response)
            }
            stopLoading()
        }
    }
}