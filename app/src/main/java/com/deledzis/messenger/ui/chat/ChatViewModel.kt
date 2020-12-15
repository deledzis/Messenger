package com.deledzis.messenger.ui.chat

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.App
import com.deledzis.messenger.base.BaseViewModel
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.util.UploadRequestBody
import com.deledzis.messenger.util.fromJson
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class ChatViewModel(private val chatId: Int) : BaseViewModel(),
    UploadRequestBody.UploadCallback {
    override val repository: ChatRepository = ChatRepository(App.injector.api())

    val text = MutableLiveData<String>()
    val type = MutableLiveData(false) // false -- text, true -- file
    val error = MutableLiveData<String>()
    val messages = MutableLiveData<List<Message>>()
    val newMessages = MutableLiveData<Boolean>()
    val uploadProgress = MutableLiveData<Int>()
    val sent = MutableLiveData<Boolean>()
    val uploading = MutableLiveData<Boolean>()

    private var uploadTask: UploadTask? = null

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
                type = false, // text
                content = text.value!!
            )
            if (response == null) {
                error.postValue("Не удалось отправить сообщение")
            } else {
                getChat()
                sent.postValue(true)
                text.postValue(null)
            }
            stopLoading()
        }
    }

    fun sendImage(uri: Uri) {
        scope.launch {
            val response = repository.sendTextMessage(
                chatId = chatId,
                authorId = App.injector.userData().authorizedUser?.id ?: 0,
                type = true, // image
                content = uri.toString()
            )
            if (response == null) {
                error.postValue("Не удалось отправить сообщение")
            } else {
                getChat()
                text.postValue(null)
            }
            uploading.postValue(false)
            stopLoading()
        }
    }

    fun uploadFileToFirebase(uri: Uri, storageRef: StorageReference) {
        uploading.postValue(true)
        val fileRef = storageRef.child("user-files/${uri.lastPathSegment}")

        val metadata = storageMetadata {
            contentType = if (uri.lastPathSegment?.endsWith("png") == true) {
                "image/png"
            } else {
                "image/jpeg"
            }
        }

        uploadTask = fileRef.putFile(uri, metadata)

        uploadTask!!.addOnProgressListener {
            val progress = (100.0 * it.bytesTransferred) / it.totalByteCount
            uploadProgress.postValue(progress.roundToInt())
        }

        uploadTask!!.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let { throw it }
            }
            fileRef.downloadUrl
        }.addOnSuccessListener {
            it?.let { uri -> sendImage(uri) } ?: kotlin.run {
                error.postValue("Не удалось отправить сообщение")
                uploading.postValue(false)
            }
        }.addOnFailureListener {
            if (it is StorageException) {
                error.postValue("Максимальный размер изображения 5 МБ")
            } else {
                error.postValue("Не удалось отправить сообщение")
            }
            uploading.postValue(false)
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

    override fun onCleared() {
        uploadTask?.cancel()
        super.onCleared()
    }
}