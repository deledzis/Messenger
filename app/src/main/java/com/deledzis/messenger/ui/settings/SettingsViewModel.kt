package com.deledzis.messenger.ui.settings

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.AuthorizedUser
import kotlinx.coroutines.launch

class SettingsViewModel: BaseViewModel() {
    override val repository: SettingsRepository = SettingsRepository(App.injector.api())

    val userData = MutableLiveData<AuthorizedUser>()
    val error = MutableLiveData<String>()

    var username = MutableLiveData(App.injector.userData().authorizedUser?.username)
    var nickname = MutableLiveData(App.injector.userData().authorizedUser?.nickname)
    var password = MutableLiveData("")
    var newPassword = MutableLiveData("")

    fun updateUserData() {
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
            if (!newPassword.value.isNullOrBlank() && password.value.isNullOrBlank()) {
                error.postValue("Для изменения пароля нужно ввести текущий пароль")
                return@launch
            }
            if (newPassword.value?.length !in (8 .. 32) && !password.value.isNullOrBlank()) {
                error.postValue("Пароль не может содержать более 32 символов")
                return@launch
            }
            val response = repository.updateUserData(
                id= App.injector.userData().authorizedUser?.id ?: 0,
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
        }
        stopLoading()
    }
}
