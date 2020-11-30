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
            if (username.value.isNullOrBlank()) {
                _error.postValue("Нужно ввести логин")
                return@launch
            }
            if (username.value?.length !in (3 .. 32)) {
                _error.postValue("Логин не может содержать более 32 символов")
                return@launch
            }
            if (nickname.value?.length !in (3 .. 32)) {
                _error.postValue("Имя не может содержать более 32 символов")
                return@launch
            }
            if (password.value.isNullOrBlank()) {
                _error.postValue("Нужно ввести пароль")
                return@launch
            }
            if (password.value?.length !in (8 .. 32)) {
                _error.postValue("Пароль не может содержать более 32 символов")
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