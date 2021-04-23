package com.deledzis.messenger.presentation.features.main

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.domain.model.request.auth.DeleteAccountRequest
import com.deledzis.messenger.domain.model.response.auth.DeleteAccountResponse
import com.deledzis.messenger.domain.usecase.auth.DeleteAccountUseCase
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val userData: BaseUserData
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(deleteAccountUseCase.receiveChannel)

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
        when (data) {
            is DeleteAccountResponse -> handleDeleteAccount(data)
        }
    }

    private fun handleDeleteAccount(data: DeleteAccountResponse) {
        if (data.response.errorCode == 0) {
            saveUser(null)
        }
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

    fun deleteAccount() {
        deleteAccountUseCase(
            params = DeleteAccountRequest(
                username = user.value?.username ?: return
            )
        )
    }
}