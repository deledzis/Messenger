package com.deledzis.messenger.presentation.features.search

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.domain.model.request.messages.GetChatMessagesRequest
import com.deledzis.messenger.domain.model.response.messages.GetChatMessagesResponse
import com.deledzis.messenger.domain.usecase.messages.GetChatMessagesUseCase
import com.deledzis.messenger.infrastructure.util.debounce
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(
            getChatMessagesUseCase.receiveChannel
        )

    val searchText = MutableLiveData<String>()
    val searchTextDebounced: MediatorLiveData<String> = MediatorLiveData<String>()
    val messages = MutableLiveData<List<Message>>()
    val getChatMessagesError = MutableLiveData<Int>()

    private var chatId: Int? = null

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
            is GetChatMessagesResponse -> handleGetChatMessagesResponse(data)
        }
    }

    override fun handleFailure(error: Error) {
        super.handleFailure(error)
        error.exception?.asHttpError?.let {
            when {
                it.isGeneralError -> getChatMessagesError.value = R.string.error_api_400
                it.isAuthError -> getChatMessagesError.value = R.string.error_api_406
                it.isInterlocutorNotFoundError -> getChatMessagesError.value =
                    R.string.error_api_407
                it.isChatNotFoundError -> getChatMessagesError.value = R.string.error_api_408
                else -> Unit
            }
        }
    }

    fun init(chatId: Int) {
        this.chatId = chatId
        searchTextDebounced.addSource(
            searchText.distinctUntilChanged().debounce(500L)
        ) {
            if (it.isBlank()) {
                messages.value = emptyList()
            } else {
                startLoading()
                search(search = it)
            }
        }
    }

    fun search(search: String? = null) {
        getChatMessagesError.value = 0
        getChatMessagesUseCase(
            params = GetChatMessagesRequest(
                chatId = chatId ?: return,
                search = search
            )
        )
    }

    private fun handleGetChatMessagesResponse(data: GetChatMessagesResponse) {
        val messages = data.response.items
        this.messages.value = messages
        stopLoading()
    }
}