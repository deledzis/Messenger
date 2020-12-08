package com.deledzis.messenger.ui.register

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.Auth
import kotlinx.coroutines.launch

class RegisterViewModel : BaseViewModel() {
    override val repository: RegisterRepository = RegisterRepository(App.injector.api())

    var username = MutableLiveData("")
    var nickname = MutableLiveData("")
    var password = MutableLiveData("")
    val error = MutableLiveData<String>()
    val userData = MutableLiveData<Auth>()

    fun register() {
        startLoading()
        scope.launch {
            if (username.value.isNullOrBlank()) {
                error.postValue("Нужно ввести логин")
                return@launch
            }
            if (username.value?.length !in (3 .. 32)) {
                error.postValue("Логин не может содержать более 32 символов")
                return@launch
            }
            if (nickname.value?.length !in (3 .. 32)) {
                error.postValue("Имя не может содержать более 32 символов")
                return@launch
            }
            if (password.value.isNullOrBlank()) {
                error.postValue("Нужно ввести пароль")
                return@launch
            }
            if (password.value?.length !in (8 .. 32)) {
                error.postValue("Пароль не может содержать более 32 символов")
                return@launch
            }
            val response = repository.register(
                username = username.value!!,
                nickname = nickname.value,
                password = password.value!!
            )
            if (response == null) {
                error.postValue("Не удалось зарегистрироваться с предоставленными данными")
            } else {
                userData.postValue(response)
            }
            stopLoading()
        }
    }
}