package com.deledzis.messenger.presentation.screens.addchat

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.domain.model.entity.user.User
import com.deledzis.messenger.domain.model.request.chats.AddChatRequest
import com.deledzis.messenger.domain.model.request.user.GetUsersRequest
import com.deledzis.messenger.domain.model.response.chats.AddChatResponse
import com.deledzis.messenger.domain.model.response.user.GetUsersResponse
import com.deledzis.messenger.domain.usecase.chats.AddChatUseCase
import com.deledzis.messenger.domain.usecase.user.GetUsersUseCase
import com.deledzis.messenger.infrastructure.util.debounce
import com.deledzis.messenger.presentation.base.BaseViewModel
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject

class AddChatViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val addChatUseCase: AddChatUseCase
) : BaseViewModel() {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(
            getUsersUseCase.receiveChannel,
            addChatUseCase.receiveChannel
        )

    val searchText: MutableLiveData<String> = MutableLiveData<String>()
    val searchTextDebounced: MediatorLiveData<String> = MediatorLiveData<String>()
    val users: MutableLiveData<List<User>> = MutableLiveData<List<User>>()
    val addedChat: MutableLiveData<Chat> = MutableLiveData<Chat>()

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
            is GetUsersResponse -> handleGetUsersResponse(data)
            is AddChatResponse -> handleAddChatResponse(data)
        }
    }

    fun init() {
        searchTextDebounced.addSource(
            searchText.distinctUntilChanged().debounce(500L)
        ) {
            if (it.isBlank()) {
                users.value = emptyList()
            } else {
                startLoading()
                search(search = it)
            }
        }
    }

    fun search(search: String? = null) {
        getUsersUseCase(params = GetUsersRequest(search = search))
    }

    fun addChat(user: User) {
        startLoading()
        addChatUseCase(params = AddChatRequest(interlocutorId = user.id ?: return))
    }

    private fun handleAddChatResponse(data: AddChatResponse) {
        if (data.response.id != 0) {
            handleAddChatOkResponse(data.response)
        }
        stopLoading()
    }

    private fun handleAddChatOkResponse(response: Chat) {
        addedChat.value = response
    }

    private fun handleGetUsersResponse(data: GetUsersResponse) {
        val users = data.response.items
        if (users != null) {
            handleGetUsersOkResponse(users)
        }
        stopLoading()
    }

    private fun handleGetUsersOkResponse(users: List<User>) {
        this.users.value = users
    }
}