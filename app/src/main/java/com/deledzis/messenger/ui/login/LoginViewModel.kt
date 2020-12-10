package com.deledzis.messenger.ui.login

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.AuthorizedUser
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    override val repository: LoginRepository = LoginRepository(App.injector.api())

    val userData = MutableLiveData<AuthorizedUser>()
    val error = MutableLiveData<String>()

    var username = MutableLiveData<String>()
    val usernameError = MutableLiveData<String>()

    var password = MutableLiveData<String>()
    val passwordError = MutableLiveData<String>()

    private fun clearErrors() {
        error.postValue(null)
        usernameError.postValue(null)
        passwordError.postValue(null)
    }

    fun login() {
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
            val response = repository.login(
                email = username.value!!,
                password = password.value!!
            )
            if (response == null) {
                error.postValue("Не удалось войти с предоставленными учетными данными")
            } else {
                userData.postValue(response)
            }
            stopLoading()
        }
    }
}