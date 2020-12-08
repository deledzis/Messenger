package com.deledzis.messenger.ui.login

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.Auth
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    override val repository: LoginRepository = LoginRepository(App.injector.api())

    var username = MutableLiveData("")
    var password = MutableLiveData("")
    val error = MutableLiveData<String>()
    val userData = MutableLiveData<Auth>()

    fun login() {
        startLoading()
        scope.launch {
            if (username.value.isNullOrBlank() || password.value.isNullOrBlank()) {
                error.postValue("Нужно ввести имя пользователя и пароль")
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