package com.deledzis.messenger.ui.register

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.Auth
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {
    override val repository: RegisterRepository = RegisterRepository(App.injector.api())

    private var _error = MutableLiveData<String>()
    val error = _error

    private var _userData = MutableLiveData<Auth>()
    val userData = _userData

    var username = MutableLiveData("")
    var nickname = MutableLiveData("")
    var password = MutableLiveData("")

    fun register() {
        startLoading()
        scope.launch {
            if (username.value.isNullOrBlank() || password.value.isNullOrBlank()) {
                _error.postValue("Нужно ввести имя пользователя и пароль и пароль")
                return@launch
            }
            val userData = repository.register(
                username = username.value!!,
                nickname = nickname.value,
                password = password.value!!
            )
            if (userData == null) {
                _error.postValue("Не удалось зарегистрироваться с предоставленными данными")
            } else {
                _userData.postValue(userData)
            }
            stopLoading()
        }
    }
}