package com.deledzis.messenger.ui.login

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.Auth
import kotlinx.coroutines.launch

class LoginViewModel : BaseViewModel() {
    override val repository: LoginRepository = LoginRepository(App.injector.api())

    private var _error = MutableLiveData<String>()
    val error = _error

    private var _userData = MutableLiveData<Auth>()
    val userData = _userData

    var username = MutableLiveData("")
    var password = MutableLiveData("")

    fun login() {
        startLoading()
        scope.launch {
            if (username.value.isNullOrBlank() || password.value.isNullOrBlank()) {
                _error.postValue("Нужно ввести имя пользователя и пароль")
                return@launch
            }
            val userData = repository.login(
                email = username.value!!,
                password = password.value!!
            )
            if (userData == null) {
                _error.postValue("Не удалось войти с предоставленными учетными данными")
            } else {
                _userData.postValue(userData)
            }
            stopLoading()
        }
    }
}