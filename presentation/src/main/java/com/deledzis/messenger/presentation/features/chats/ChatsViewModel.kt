package com.deledzis.messenger.presentation.features.chats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.deledzis.messenger.OpenForTesting
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.domain.model.response.chats.GetChatsResponse
import com.deledzis.messenger.domain.usecase.chats.GetChatsUseCase
import com.deledzis.messenger.presentation.base.AssistedSavedStateViewModelFactory
import com.deledzis.messenger.presentation.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber

@OpenForTesting
class ChatsViewModel @AssistedInject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    @AssistedFactory
    interface Factory : AssistedSavedStateViewModelFactory<ChatsViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): ChatsViewModel
    }

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(getChatsUseCase.receiveChannel)

    val chats = MutableLiveData<List<Chat>>()
    val chatsLoadingError = MutableLiveData<Int>()

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
        chatsLoadingError.value = 0
        if (refresh || chats.value.isNullOrEmpty()) startLoading()
        getChatsUseCase(Unit)
    }

    private fun handleGetChatsResponse(data: GetChatsResponse) {
        val chats = data.response.items
        this.chats.value = chats
        stopLoading()
    }
}