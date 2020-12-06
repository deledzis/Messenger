package com.deledzis.messenger.ui.chat

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.data.model.user.User
import com.deledzis.messenger.util.fromJson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChatViewModel(private val chatId: Int) : BaseViewModel() {
    override val repository: ChatRepository = ChatRepository(App.injector.api())

    val text = MutableLiveData<String>()
    val type = MutableLiveData(false) // false -- text, true -- file
    val error = MutableLiveData<String>()
    val messages = MutableLiveData<List<Message>>()

    fun getChat() {
        error.postValue(null)
        if (messages.value.isNullOrEmpty()) startLoading()
        scope.launch {
            val response = repository.getChat(chatId)
            // artificial delay for progress while loading demonstration purposes
            delay(1000L)
            if (response == null) {
                //error.postValue("Не удалось обновить сообщения")
                messages.postValue(generateMockMessages())
            } else {
                messages.postValue(response.messages)
            }
            stopLoading()
        }
    }

    fun sendMessage() {
        if (text.value.isNullOrBlank()) {
            error.postValue("Пустое сообщение!")
            return
        }
        scope.launch {
            val response = repository.sendTextMessage(
                chatId = chatId,
                authorId = App.injector.userData().auth?.userId ?: 0,
                type = type.value ?: false,
                content = text.value!!
            )
            // artificial delay for progress while loading demonstration purposes
            delay(1000L)
            if (response == null) {
                error.postValue("Не удалось отправить сообщение")
            } else {
                getChat()
            }
            stopLoading()
        }
    }

    fun handleBackgroundMessagesResult(messagesJson: String?) {
        messagesJson ?: return
        val list = fromJson<List<Message>>(messagesJson)
        messages.postValue(list)
        if (list.isNotEmpty()) {
            error.postValue(null)
        }
    }

    companion object {
        fun generateMockMessages() = listOf(
            Message(
                id = 0,
                type = true,
                content = "",
                fileName = "some file name.docx",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(
                    id = 0,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 0,
                type = true,
                content = "",
                fileName = "some file name.docx",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(
                    id = 0,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 0,
                type = true,
                content = "",
                fileName = "some file name.docx",
                date = "2020-12-05T12:25:32",
                chatId = 0,
                author = User(
                    id = 0,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 1,
                type = false,
                content = "very very very very very very very very very very very very very very very very very very very very very very very very very very long text",
                date = "2020-12-04T12:25:32",
                chatId = 0,
                author = User(
                    id = 0,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 2,
                type = false,
                content = "some text ",
                date = "2020-12-03T12:25:32",
                chatId = 0,
                author = User(
                    id = 1,
                    username = "",
                    nickname = ""
                )
            ),
            Message(
                id = 3,
                type = true,
                content = "test content",
                fileName = "very very very very very very very very long name.docx",
                date = "2020-12-02T12:25:32",
                chatId = 0,
                author = User(
                    id = 1,
                    username = "",
                    nickname = ""
                )
            ),
        )
    }
}