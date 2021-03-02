package com.deledzis.messenger.presentation.features.main

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class UserViewModel @Inject constructor(
    private val userData: BaseUserData
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = Channel()

    var user: MutableLiveData<Auth> = MutableLiveData<Auth>(userData.getAuthUser())

    override suspend fun resolve(value: Response<Entity, Error>) {
        value.handleResult(
            stateBlock = ::handleState,
            failureBlock = ::handleFailure,
            successBlock = ::handleSuccess
        )
    }

    private fun handleSuccess(data: Any?) {
        Timber.i("Handle Success: $data")
    }

    fun saveUser(auth: Auth?) {
        userData.doAndSaveAuthUser(auth) {
            Timber.d("Saving User $auth")
            this.user.postValue(auth)
        }
    }

    fun handleLogout() {
        saveUser(null)
    }
}