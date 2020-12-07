package com.deledzis.messenger.ui.chats

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.util.fromJson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatsViewModel : BaseViewModel() {
    override val repository: ChatsRepository = ChatsRepository(App.injector.api())

    val error = MutableLiveData<Boolean>()
    val chats = MutableLiveData<List<ChatReduced>>()

    fun getChats(refresh: Boolean = true) {
        error.postValue(null)
        if (refresh || chats.value.isNullOrEmpty()) startLoading()
        scope.launch {
            val response = repository.getChats()
            // TODO artificial delay for progress while loading demonstration purposes, to be removed
            delay(1000L)
            if (response == null) {
                // TODO to be removed, temporary for mock purposes
                if (refresh) {
                    chats.postValue(generateMockChatsList())
                } else {
                    error.postValue(true)
                }
            } else {
                chats.postValue(response.chats)
            }
            stopLoading()
        }
    }

    fun handleBackgroundChatsResult(chatsJson: String?) {
        chatsJson ?: return
        val list = fromJson<List<ChatReduced>>(chatsJson)
        chats.postValue(list)
        if (list.isNotEmpty()) {
            error.postValue(null)
        }
    }

    // TODO to be removed, temporary for mock purposes
    private fun generateMockChatsList() = listOf(
        ChatReduced(
            id = 0,
            interlocutor = User(id = 1, username = "user@name.com", nickname = "nickname"),
            lastMessage = Message(
                id = 0,
                type = false,
                content = "Test",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(id = 0, username = "Some User", nickname = "")
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 2, username = "other-user@name.com", nickname = null),
            lastMessage = Message(
                id = 0,
                type = false,
                content = "some message",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(id = 2, username = "other-user@name.com", nickname = null)
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 1, username = "user@name.com", nickname = "nickname"),
            lastMessage = Message(
                id = 0,
                type = false,
                content = "test message",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(id = 0, username = "Some User", nickname = "")
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 2, username = "other-user@name.com", nickname = null),
            lastMessage = Message(
                id = 0,
                type = false,
                content = "How are you? \uD83D\uDE42",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(id = 2, username = "other-user@name.com", nickname = null)
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 1, username = "user@name.com", nickname = "nickname"),
            lastMessage = Message(
                id = 0,
                type = true,
                content = "",
                date = "2020-12-04T12:25:32",
                chatId = 0,
                author = User(id = 1, username = "user@name.com", nickname = "nickname")
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 2, username = "other-user@name.com", nickname = null),
            lastMessage = Message(
                id = 0,
                type = true,
                content = "",
                date = "2020-12-03T12:25:32",
                chatId = 0,
                author = User(id = 0, username = "Some User", nickname = "")
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 1, username = "user@name.com", nickname = "nickname"),
            lastMessage = Message(
                id = 0,
                type = false,
                content = "some message...",
                date = "2020-12-02T12:25:32",
                chatId = 0,
                author = User(id = 1, username = "user@name.com", nickname = "nickname")
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 2, username = "other-user@name.com", nickname = null),
            lastMessage = Message(
                id = 0,
                type = false,
                content = "very very very very very very very very very very very very very very very very very very very very very very very very very very long",
                date = "2020-12-01T12:25:32",
                chatId = 0,
                author = User(id = 0, username = "Some User", nickname = "")
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 1, username = "user@name.com", nickname = "nickname"),
            lastMessage = Message(
                id = 0,
                type = false,
                content = "some message...",
                date = "2020-12-02T12:25:32",
                chatId = 0,
                author = User(id = 1, username = "user@name.com", nickname = "nickname")
            )
        ),
        ChatReduced(
            id = 0,
            interlocutor = User(id = 2, username = "other-user@name.com", nickname = null),
            lastMessage = Message(
                id = 0,
                type = false,
                content = "very very very very very very very very very very very very very very very very very very very very very very very very very very long",
                date = "2020-12-01T12:25:32",
                chatId = 0,
                author = User(id = 0, username = "Some User", nickname = "")
            )
        )
    )
}