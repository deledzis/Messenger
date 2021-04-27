package com.deledzis.messenger.presentation.features.chats

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.domain.model.request.chats.DeleteChatRequest
import com.deledzis.messenger.domain.model.response.chats.DeleteChatResponse
import com.deledzis.messenger.domain.model.response.chats.GetChatsResponse
import com.deledzis.messenger.domain.usecase.chats.DeleteChatUseCase
import com.deledzis.messenger.domain.usecase.chats.GetChatsUseCase
import com.deledzis.messenger.infrastructure.util.SingleEventLiveData
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.AssistedSavedStateViewModelFactory
import com.deledzis.messenger.presentation.base.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber

class ChatsViewModel @AssistedInject constructor(
    private val getChatsUseCase: GetChatsUseCase,
    private val deleteChatUseCase: DeleteChatUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    @AssistedFactory
    interface Factory : AssistedSavedStateViewModelFactory<ChatsViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): ChatsViewModel
    }

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(
            getChatsUseCase.receiveChannel,
            deleteChatUseCase.receiveChannel
        )

    val chats = MutableLiveData<List<Chat>>()
    val getChatsError = SingleEventLiveData<Int>()
    val deleteChatError = SingleEventLiveData<Int>()

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
            is DeleteChatResponse -> handleDeleteChatResponse()
        }
    }

    override fun handleFailure(error: Error) {
        super.handleFailure(error)
        error.exception?.asHttpError?.let {
            when {
                it.isGeneralError -> getChatsError.postValue(R.string.error_api_400)
                it.isAuthError -> getChatsError.postValue(R.string.error_api_406)
                it.isInterlocutorNotFoundError -> getChatsError.postValue(R.string.error_api_407)
                it.isMissingChatError -> deleteChatError.postValue(R.string.error_api_411)
                it.isDeleteChatError -> deleteChatError.postValue(R.string.error_api_420)
                else -> Unit
            }
        }
    }

    private fun clearErrors() {
        getChatsError.postValue(0)
        deleteChatError.postValue(0)
    }

    fun getChats(refresh: Boolean = true) {
        if (refresh) {
            clearErrors()
            startLoading()
        }
        getChatsUseCase(Unit)
    }

    fun deleteChat(chatId: Int) {
        deleteChatUseCase(DeleteChatRequest(chatId))
    }

    private fun handleGetChatsResponse(data: GetChatsResponse) {
        val chats = data.response.items
        this.chats.value = chats
        stopLoading()
    }

    private fun handleDeleteChatResponse() = getChats(refresh = false)
}