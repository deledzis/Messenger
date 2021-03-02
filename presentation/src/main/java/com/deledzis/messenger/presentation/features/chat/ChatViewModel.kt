package com.deledzis.messenger.presentation.features.chat

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.common.extensions.mergeChannels
import com.deledzis.messenger.common.usecase.Error
import com.deledzis.messenger.common.usecase.Response
import com.deledzis.messenger.domain.model.entity.Entity
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.domain.model.request.messages.GetChatMessagesRequest
import com.deledzis.messenger.domain.model.request.messages.SendMessageRequest
import com.deledzis.messenger.domain.model.response.messages.GetChatMessagesResponse
import com.deledzis.messenger.domain.model.response.messages.SendMessageResponse
import com.deledzis.messenger.domain.usecase.messages.GetChatMessagesUseCase
import com.deledzis.messenger.domain.usecase.messages.SendMessageUseCase
import com.deledzis.messenger.infrastructure.util.UploadRequestBody
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseViewModel
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.channels.ReceiveChannel
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.roundToInt

class ChatViewModel @Inject constructor(
    private val getChatMessagesUseCase: GetChatMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
) : BaseViewModel(),
    UploadRequestBody.UploadCallback {

    override val receiveChannel: ReceiveChannel<Response<Entity, Error>>
        get() = mergeChannels(
            getChatMessagesUseCase.receiveChannel,
            sendMessageUseCase.receiveChannel
        )

    val text = MutableLiveData<String>()
    val type = MutableLiveData<Int>(Message.TYPE_TEXT)
    val messages = MutableLiveData<List<Message>>()
    val messagesError = MutableLiveData<Int>()
    val newMessages = MutableLiveData<Boolean>()
    val uploadProgress = MutableLiveData<Int>()
    val sent = MutableLiveData<Boolean>()
    val sentError = MutableLiveData<Int>()
    val uploading = MutableLiveData<Boolean>()

    private var chatId: Int? = null
    private var uploadTask: UploadTask? = null

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
            is SendMessageResponse -> handleSendMessageResponse(data)
        }
    }

    fun init(chatId: Int) {
        this.chatId = chatId
        getChatMessages()
    }

    fun getChatMessages() {
        messagesError.value = 0
        if (messages.value.isNullOrEmpty()) startLoading()

        getChatMessagesUseCase(
            params = GetChatMessagesRequest(
                chatId = chatId ?: return,
                search = null
            )
        )
    }

    fun sendMessage() {
        if (text.value.isNullOrBlank()) {
            sentError.value = R.string.error_empty_message
            return
        }

        sendMessageUseCase(
            params = SendMessageRequest(
                chatId = chatId ?: return,
                type = Message.TYPE_TEXT,
                content = text.value ?: return
            )
        )
    }

    private fun sendImage(uri: Uri) {
        sendMessageUseCase(
            params = SendMessageRequest(
                chatId = chatId ?: return,
                type = Message.TYPE_IMAGE,
                content = uri.toString()
            )
        )
    }

    private fun handleGetChatMessagesResponse(data: GetChatMessagesResponse) {
        val messages = data.response.items
        if (messages != null) {
            handleGetChatMessagesOkResponse(messages)
        } else {
            messagesError.value = R.string.error_get_messages
        }
        stopLoading()
    }

    private fun handleGetChatMessagesOkResponse(messages: List<Message>) {
        postMessages(messages)
    }

    private fun handleSendMessageResponse(data: SendMessageResponse) {
        if (data.response.errorCode == 0) {
            handleSendMessageOkResponse()
        } else {
            sentError.value = R.string.error_message_sending
        }
        uploading.value = false
        stopLoading()
    }

    private fun handleSendMessageOkResponse() {
        sent.value = true
        text.value = ""
        getChatMessages()
    }

    fun uploadFileToFirebase(uri: Uri, storageRef: StorageReference) {
        uploading.value = true
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
                sentError.postValue(R.string.error_message_sending)
                uploading.postValue(false)
            }
        }.addOnFailureListener {
            if (it is StorageException) {
                sentError.postValue(R.string.error_file_size_exceed)
            } else {
                sentError.postValue(R.string.error_message_sending)
            }
            uploading.postValue(false)
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