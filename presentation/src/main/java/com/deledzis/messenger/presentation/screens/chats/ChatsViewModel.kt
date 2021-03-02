package com.deledzis.messenger.presentation.screens.chats

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.domain.model.response.chats.GetChatsResponse
import com.deledzis.messenger.domain.usecase.chats.GetChatsUseCase
import com.deledzis.messenger.infrastructure.util.fromJson
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject

class ChatsViewModel @Inject constructor(
    private val getChatsUseCase: GetChatsUseCase
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(getChatsUseCase.receiveChannel)

    val chats = MutableLiveData<List<Chat>>()

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
            is GetChatsResponse -> handleGetChatsResponse(data)
        }
    }

    fun getChats(refresh: Boolean = true) {
        loadingError.value = false
        if (refresh || chats.value.isNullOrEmpty()) startLoading()
        getChatsUseCase(Unit)
    }

    private fun handleGetChatsResponse(data: GetChatsResponse) {
        val chats = data.response.items
        if (chats != null) {
            handleLoginOkResponse(chats)
        } else {
            logError(
                errorCode = data.response.errorCode,
                originMessage = data.response.message,
                normalizedMessage = "Не удалось войти с предоставленными учетными данными"
            )
            loadingError.value = true
        }
        stopLoading()
    }

    private fun handleLoginOkResponse(chats: List<Chat>) {
        this.chats.value = chats
    }

    fun handleBackgroundChatsResult(chatsJson: String?) {
        chatsJson ?: return
        val list = fromJson<List<Chat>>(chatsJson)
        chats.postValue(list)
        if (list.isNotEmpty()) {
            loadingError.postValue(false)
        }
    }
}