package com.deledzis.messenger.ui.chat

import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.util.UploadRequestBody
import com.deledzis.messenger.util.fromJson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class ChatViewModel(private val chatId: Int) : BaseViewModel(),
    UploadRequestBody.UploadCallback {
    override val repository: ChatRepository = ChatRepository(App.injector.api())

    val text = MutableLiveData<String>()
    val type = MutableLiveData(false) // false -- text, true -- file
    val error = MutableLiveData<String>()
    val messages = MutableLiveData<List<Message>>()
    val newMessages = MutableLiveData<Boolean>()
    val uploadProgress = MutableLiveData<Int>()

    fun getChat() {
        error.postValue(null)
        if (messages.value.isNullOrEmpty()) startLoading()
        scope.launch {
            val response = repository.getChat(chatId)
            if (response == null) {
                error.postValue("Не удалось обновить сообщения")
            } else {
                postMessages(response.messages)
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
                authorId = App.injector.userData().authorizedUser?.id ?: 0,
                type = type.value ?: false,
                content = text.value!!
            )
            if (response == null) {
                error.postValue("Не удалось отправить сообщение")
            } else {
                getChat()
                text.postValue(null)
            }
            stopLoading()
        }
    }

    fun uploadFile(file: File) {
        scope.launch {
            val body = UploadRequestBody(file, "image", this@ChatViewModel)
            val image = MultipartBody.Part.createFormData(
                "image",
                file.name,
                body
            )
            val response = repository.sendImageMessage(
                chatId = chatId,
                authorId = (App.injector.userData().authorizedUser?.id ?: 0).toString()
                    .toRequestBody("multipart/form-data".toMediaTypeOrNull()),
                image = image
            )
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
        postMessages(list)
        if (list.isNotEmpty()) {
            error.postValue(null)
        }
    }

    private fun postMessages(list: List<Message>) {
        messages.postValue(list)
        newMessages.postValue(checkHasNewMessages(list))
    }

    private fun checkHasNewMessages(newList: List<Message>): Boolean {
        val originalList = messages.value ?: emptyList()

        val sum = originalList + newList
        return sum.groupBy { it.id }
            .filter { it.value.size == 1 }
            .flatMap { it.value }
            .isNotEmpty()
    }

    override fun onProgressUpdate(percentage: Int) {
        uploadProgress.postValue(percentage)
    }
}