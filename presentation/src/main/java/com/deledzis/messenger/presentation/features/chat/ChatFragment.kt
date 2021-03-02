package com.deledzis.messenger.presentation.features.chat

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.deledzis.messenger.common.Constants
import com.deledzis.messenger.common.Constants.MESSAGES_PERIODIC_DELAY
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.infrastructure.extensions.animateGone
import com.deledzis.messenger.infrastructure.extensions.animateShow
import com.deledzis.messenger.infrastructure.extensions.hideSoftKeyboard
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseFragment
import com.deledzis.messenger.presentation.databinding.FragmentChatBinding
import timber.log.Timber
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatFragment @Inject constructor() :
    BaseFragment<ChatViewModel, FragmentChatBinding>(layoutId = R.layout.fragment_chat),
    ChatActionsHandler {

    override val viewModel: ChatViewModel by viewModels()
    private val args: ChatFragmentArgs by navArgs()
    private lateinit var adapter: MessagesAdapter
    private var scheduledFuture: ScheduledFuture<*>? = null

    @Inject
    lateinit var userData: BaseUserData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(chatId = args.chatId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dataBinding.viewModel = viewModel
        dataBinding.controller = this
        dataBinding.chatTitle = args.chatTitle

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MessagesAdapter(
            userId = userData.getAuthUser()?.id ?: throw Exception(),
            controller = ::onSelected
        )
        dataBinding.rvMessages.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        dataBinding.rvMessages.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        if (!Constants.MOCK_BUILD) {
            startPeriodicWorker()
        }
    }

    override fun onStop() {
        if (!Constants.MOCK_BUILD) {
            stopPeriodicWorker()
        }
        super.onStop()
    }

    override fun bindObservers() {
        viewModel.getChatMessages()
        viewModel.messages.observe(viewLifecycleOwner, ::messagesObserver)
        viewModel.newMessages.observe(viewLifecycleOwner, ::newMessagesObserver)
        viewModel.uploading.observe(viewLifecycleOwner, ::uploadingObserver)
        viewModel.sent.observe(viewLifecycleOwner, ::sentObserver)
        viewModel.messagesError.observe(viewLifecycleOwner, ::errorObserver)
        viewModel.sentError.observe(viewLifecycleOwner, ::errorObserver)
    }

    private fun messagesObserver(list: List<Message>?) {
        adapter.messages = list ?: return
        dataBinding.rvMessages.setItemViewCacheSize(list.size)
    }

    private fun newMessagesObserver(hasNewMessages: Boolean?) {
        if (hasNewMessages == true) {
            dataBinding.rvMessages.scrollToPosition(0)
        }
    }

    private fun uploadingObserver(uploading: Boolean?) {
        toggleSendProgress(uploading ?: false)
        toggleUploadProgress(uploading ?: false)
    }

    private fun errorObserver(@StringRes error: Int?) {
        hideSoftKeyboard()
        if (error == null) return
        val errorMessage = getString(error)
        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show()
    }

    private fun sentObserver(sent: Boolean?) {
        toggleSendProgress(sent ?: false)
    }

    override fun onBackClicked(view: View) {
        findNavController().popBackStack()
    }

    override fun onSearchClicked(view: View) {
        val action =
            ChatFragmentDirections.actionChatFragmentToSearchFragment(chatId = args.chatId)
        findNavController().navigate(action)
    }

    override fun onAttachmentClicked(view: View) {
        if (isStoragePermissionGranted()) {
            startFilePicker()
        }
    }

    override fun onSendClicked(view: View) {
        toggleSendProgress(true)
        viewModel.sendMessage()
    }

    fun onSelected(message: Message) {
        // TODO
    }

    private fun toggleSendProgress(progress: Boolean) {
        if (progress) {
            dataBinding.icSend.animateGone()
            dataBinding.sendProgress.animateShow()
        } else {
            dataBinding.icSend.animateShow()
            dataBinding.sendProgress.animateGone()
        }
    }

    private fun toggleUploadProgress(progress: Boolean) {
        if (progress) {
            dataBinding.tilMessage.animateGone()
            dataBinding.progressUpload.animateShow()
        } else {
            dataBinding.tilMessage.animateShow()
            dataBinding.progressUpload.animateGone()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startFilePicker()
        } else {
            startSnackbar(
                getString(R.string.error_storage_permissions),
                indefinite = true,
                retryAction = { startFilePicker() }
            )
        }
    }

    private fun startFilePicker() {
        Intent(Intent.ACTION_PICK).also {
            it.type = "image/*"
            val mimeTypes = arrayOf("image/jpeg", "image/png")
            it.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            startActivityForResult(it, FILE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return
        when (requestCode) {
            FILE_REQUEST_CODE -> {
                Timber.e("selected image uri: ${data.data}")
                data.data?.let { uploadImage(it) }
            }
        }
    }

    private fun startPeriodicWorker() {
        if (scheduledFuture != null) return

        // Here we're using ScheduledThreadPoolExecutor too run periodic background tasks
        // in each of which we run separate CoroutineWorker using Android WorkManager library
        val exec = ScheduledThreadPoolExecutor(1)
        val runnableCode = Runnable { runSilentRefreshChatWorker() }
        scheduledFuture = exec.scheduleWithFixedDelay(
            runnableCode,
            0L, // first task runs immediately
            MESSAGES_PERIODIC_DELAY, // each next task runs after terminating of previous one with delay
            TimeUnit.MILLISECONDS
        )
    }

    private fun stopPeriodicWorker() {
        // cancel all pending tasks, but allowing to finish current working
        scheduledFuture?.cancel(false)
        scheduledFuture = null
    }

    private fun runSilentRefreshChatWorker() {
        viewModel.getChatMessages()
    }

    private fun uploadImage(uri: Uri) {
        viewModel.uploadFileToFirebase(uri, activity.storageRef)
    }

    companion object {
        const val FILE_REQUEST_CODE = 1234
    }
}