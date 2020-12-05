package com.deledzis.messenger.ui.chat

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.data.model.user.User
import com.deledzis.messenger.databinding.FragmentChatBinding
import com.deledzis.messenger.util.MESSAGES_PERIODIC_DELAY
import com.deledzis.messenger.util.extensions.viewModelFactory
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ChatFragment(private val chatId: Int) : BaseFragment(),
    ChatActionsHandler,
    MessageItemActionsHandler {

    private lateinit var dataBinding: FragmentChatBinding
    private lateinit var adapter: MessagesAdapter
    private var scheduledFuture: ScheduledFuture<*>? = null

    private val viewModel: ChatViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { ChatViewModel(chatId) }
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
        dataBinding.controller = this

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        adapter = MessagesAdapter(App.injector.userData().auth?.userId!!, this)
        // TODO remove when backend work, mock
        adapter = MessagesAdapter(0, this)
        dataBinding.rvChats.layoutManager = LinearLayoutManager(activity)
        dataBinding.rvChats.adapter = adapter
    }

    override fun bindObservers() {
        viewModel.getChat()
        viewModel.messages.observe(viewLifecycleOwner, {
            Log.e("TAG", "Messages: $it")
            adapter.messages = it ?: return@observe
        })
        viewModel.error.observe(viewLifecycleOwner, {
            Log.e("TAG", "Error: $it")
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            // TODO mock data, to be removed
            adapter.messages = listOf(
                Message(
                    id = 0,
                    type = false,
                    content = "",
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
                    type = true,
                    content = "",
                    date = "2020-12-05T12:25:32",
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
                    content = "",
                    date = "2020-12-05T12:25:32",
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
                    content = "",
                    date = "2020-12-05T12:25:32",
                    chatId = 0,
                    author = User(
                        id = 1,
                        username = "",
                        nickname = ""
                    )
                ),
            )
        })

        startPeriodicWorker()
    }

    override fun onBackClicked(view: View) {
        activity.removeFragment()
    }

    override fun onSearchClicked(view: View) {
        // TODO add search fragment
    }

    override fun onAttachmentClicked(view: View) {
        // TODO start file picker intent and handle result
    }

    override fun onSelected(message: Message) {
        // TODO show dialog
    }

    override fun onDestroy() {
        stopPeriodicWorker()
        super.onDestroy()
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
        val inputData: Data = workDataOf("CHAT_ID" to chatId)
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
                        Log.e("TAG", "Observed info: $info")
                        val result = info.outputData.getString("MESSAGES")
                        viewModel.handleBackgroundMessagesResult(result)
                    }
                })
        }
    }
}