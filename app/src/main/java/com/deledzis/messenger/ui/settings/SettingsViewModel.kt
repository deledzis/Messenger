package com.deledzis.messenger.ui.settings

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.Auth
import kotlinx.coroutines.launch

class SettingsViewModel: BaseViewModel() {
    override val repository: SettingsRepository = SettingsRepository(App.injector.api())

    private var _userData = MutableLiveData<Auth>()
    val userData = _userData

    private var _error = MutableLiveData<String>()
    val error = _error

    var username = MutableLiveData(userData.value?.username)
    var nickname = MutableLiveData(userData.value?.nickname)
    var password = MutableLiveData("")
    var new_password = MutableLiveData("")


    fun updateUserData() {
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
            if (!new_password.value.isNullOrBlank() && password.value.isNullOrBlank()) {
                _error.postValue("Для изменения пароля нужно ввести текущий пароль")
                return@launch
            }
            if (new_password.value?.length !in (8 .. 32) && !password.value.isNullOrBlank()) {
                _error.postValue("Пароль не может содержать более 32 символов")
                return@launch
            }
            val userData = repository.updateUserData(
                id= App.injector.userData().auth?.userId ?: 0,
                username = username.value!!,
                nickname = nickname.value,
                password = password.value,
                new_password = new_password.value
            )
            if (userData == null) {
                _error.postValue("Не удалось изменить данные аккаунта")
            } else {
                _userData.postValue(userData)
            }
        }
        stopLoading()
    }
}
