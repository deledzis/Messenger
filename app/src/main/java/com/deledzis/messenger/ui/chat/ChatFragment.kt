package com.deledzis.messenger.ui.chat

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
import com.deledzis.messenger.App
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.databinding.FragmentChatBinding
import com.deledzis.messenger.ui.search.SearchFragment
import com.deledzis.messenger.util.*
import com.deledzis.messenger.util.extensions.animateGone
import com.deledzis.messenger.util.extensions.animateShow
import com.deledzis.messenger.util.extensions.viewModelFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ChatFragment(private val chat: ChatReduced) : BaseFragment(),
    ChatActionsHandler,
    MessageItemActionsHandler {

    private lateinit var dataBinding: FragmentChatBinding
    private lateinit var adapter: MessagesAdapter
    private var scheduledFuture: ScheduledFuture<*>? = null
    private var selectedImageUri: Uri? = null

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

        adapter = MessagesAdapter(App.injector.userData().authorizedUser?.id!!, this)
        dataBinding.rvMessages.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true)
        dataBinding.rvMessages.adapter = adapter
    }

    override fun bindObservers() {
        viewModel.getChat()
        viewModel.messages.observe(viewLifecycleOwner, {
            logi { "Messages: $it" }
            dataBinding.icSend.animateShow()
            dataBinding.sendProgress.animateGone()
            adapter.messages = it ?: return@observe
            dataBinding.rvMessages.scrollToPosition(0)
        })
        viewModel.error.observe(viewLifecycleOwner, {
            dataBinding.icSend.animateShow()
            dataBinding.sendProgress.animateGone()
            if (!it.isNullOrBlank()) {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        })

//        startPeriodicWorker()
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
        dataBinding.icSend.animateGone()
        dataBinding.sendProgress.animateShow()
        viewModel.sendMessage()
    }

    override fun onDestroy() {
        stopPeriodicWorker()
        super.onDestroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            logv { "Permission: ${permissions[0]}, was ${grantResults[0]}" }
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
                selectedImageUri = data.data
                loge { "selected image uri: $selectedImageUri" }
                uploadImage()
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
            .getInstance(requireContext())
            .enqueue(workRequest) // run work request

        // explicitly observing results in main thread
        // since all this code runs in a background thread
        Handler(Looper.getMainLooper()).post {
            WorkManager.getInstance(requireContext())
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

    private fun uploadImage() {
        selectedImageUri ?: return
        val parcelFileDescriptor = activity.contentResolver
            .openFileDescriptor(selectedImageUri!!, "r", null) ?: return

        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        val file = File(activity.cacheDir, activity.contentResolver.getFileName(selectedImageUri!!))
        val outputStream = FileOutputStream(file)
        inputStream.copyTo(outputStream)

        viewModel.uploadFile(file)
    }

    companion object {
        const val FILE_REQUEST_CODE = 1234
    }
}