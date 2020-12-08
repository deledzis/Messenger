package com.deledzis.messenger.ui.settings

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseRepository
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.auth.Auth

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


    fun changeUserData() {
        TODO()
//        startLoading()
//        stopLoading()
    }
}
