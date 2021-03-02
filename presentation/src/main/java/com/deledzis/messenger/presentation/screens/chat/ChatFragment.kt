package com.deledzis.messenger.presentation.screens.chat

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.deledzis.messenger.R
import com.deledzis.messenger.databinding.FragmentChatBinding
import com.deledzis.messenger.domain.model.entity.chats.ChatReduced
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.old.App
import com.deledzis.messenger.old.base.BaseFragment
import com.deledzis.messenger.old.util.*
import com.deledzis.messenger.old.util.extensions.viewModelFactory
import com.deledzis.messenger.presentation.screens.search.SearchFragment
import com.deledzis.messenger.util.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ChatFragment(private val chat: ChatReduced) : BaseFragment(),
    ChatActionsHandler,
    MessageItemActionsHandler {

    private lateinit var dataBinding: FragmentChatBinding
    private lateinit var adapter: MessagesAdapter
    private var scheduledFuture: ScheduledFuture<*>? = null

    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { ChatViewModel(chat.id) }
        )[ChatViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chat,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.viewModel = viewModel
        dataBinding.chat = chat
        dataBinding.controller = this

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MessagesAdapter(App.injector.userData().authorizedUser.id!!, this)
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
        viewModel.getChat()
        viewModel.messages.observe(viewLifecycleOwner, {
            adapter.messages = it ?: return@observe
            dataBinding.rvMessages.setItemViewCacheSize(it.size)
        })
        viewModel.newMessages.observe(viewLifecycleOwner, {
            if (it == true) {
                dataBinding.rvMessages.scrollToPosition(0)
            }
        })
        viewModel.uploading.observe(viewLifecycleOwner, {
            toggleSendProgress(it)
            toggleUploadProgress(it)
        })
        viewModel.sent.observe(viewLifecycleOwner, {
            toggleSendProgress(it)
        })
        viewModel.error.observe(viewLifecycleOwner, {
            toggleSendProgress(false)
            if (!it.isNullOrBlank()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onBackClicked(view: View) {
        activity.removeFragment()
    }

    override fun onSearchClicked(view: View) {
        activity.addFragment(
            fragment = SearchFragment(chat),
            tag = SEARCH_FRAGMENT_TAG
        )
    }

    override fun onAttachmentClicked(view: View) {
        if (isStoragePermissionGranted()) {
            startFilePicker()
        }
    }

    override fun onSelected(message: Message) {
        // TODO
    }

    override fun onSendClicked(view: View) {
        toggleSendProgress(true)
        viewModel.sendMessage()
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
                loge { "selected image uri: ${data.data}" }
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
        // building separate one time refresh chat worker
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val inputData: Data = workDataOf("CHAT_ID" to chat.id)
        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<SilentRefreshChatWorker>()
            .setConstraints(constraints)
            .setInputData(inputData)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                TimeUnit.MILLISECONDS
            )
            .build()

        WorkManager
            .getInstance(activity.applicationContext)
            .enqueue(workRequest) // run work request

        // explicitly observing results in main thread
        // since all this code runs in a background thread
        Handler(Looper.getMainLooper()).post {
            WorkManager.getInstance(activity.applicationContext)
                .getWorkInfoByIdLiveData(workRequest.id)
                .observe(this, { info ->
                    if (info != null && info.state.isFinished) {
                        logi { "Observed info: $info" }
                        val result = info.outputData.getString("MESSAGES")
                        viewModel.handleBackgroundMessagesResult(result)
                    }
                })
        }
    }

    private fun uploadImage(uri: Uri) {
        viewModel.uploadFileToFirebase(uri, activity.getStorageRef())
    }

    companion object {
        const val FILE_REQUEST_CODE = 1234
    }
}