package com.deledzis.messenger.ui.chats

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.deledzis.messenger.App
import com.deledzis.messenger.R
import com.deledzis.messenger.base.RefreshableFragment
import com.deledzis.messenger.data.model.chats.ChatReduced
import com.deledzis.messenger.databinding.FragmentChatsBinding
import com.deledzis.messenger.ui.addchat.AddChatFragment
import com.deledzis.messenger.ui.chat.ChatFragment
import com.deledzis.messenger.ui.chats.SilentRefreshChatsWorker.Companion.CHATS_KEY
import com.deledzis.messenger.ui.settings.SettingsFragment
import com.deledzis.messenger.util.*
import com.deledzis.messenger.util.extensions.viewModelFactory
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ChatsFragment : RefreshableFragment(), ChatsActionsHandler, ChatItemActionsHandler {
    private lateinit var dataBinding: FragmentChatsBinding
    private lateinit var adapter: ChatsAdapter
    private var scheduledFuture: ScheduledFuture<*>? = null

    private val viewModel: ChatsViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { ChatsViewModel() }
        )[ChatsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_chats,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.viewModel = viewModel
        dataBinding.controller = this

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = ChatsAdapter(this, App.injector.userData().authorizedUser?.id!!)
        dataBinding.rvChats.layoutManager = LinearLayoutManager(activity)
        dataBinding.rvChats.adapter = adapter

        srl = dataBinding.srl
        srl.setOnRefreshListener(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onStop() {
        stopPeriodicWorker()
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
        startPeriodicWorker()
    }

    override fun bindObservers() {
        viewModel.getChats(refresh = srl.isRefreshing)
        viewModel.chats.observe(viewLifecycleOwner, {
            srl.isRefreshing = false
            adapter.chats = it ?: return@observe
        })
        viewModel.error.observe(viewLifecycleOwner, {
            srl.isRefreshing = false
            it?.let {
                startSnackbar(
                    text = getString(R.string.error_get_chats),
                    indefinite = true
                ) { viewModel.getChats(refresh = true) }
            } ?: run { stopSnackbar() }
        })
    }

    override fun onAddChatClicked(view: View) {
        stopSnackbar()
        activity.addFragment(
            fragment = AddChatFragment(),
            tag = ADD_CHAT_FRAGMENT_TAG
        )
    }

    override fun onSettingsClicked(view: View) {
        stopSnackbar()
        activity.addFragment(
            fragment = SettingsFragment(),
            tag = SETTINGS_FRAGMENT_TAG
        )
    }

    override fun onSelected(chat: ChatReduced) {
        stopSnackbar()
        activity.addFragment(
            fragment = ChatFragment(chat),
            tag = CHAT_FRAGMENT_TAG
        )
    }

    override fun onRefresh() {
        stopPeriodicWorker()
        stopSnackbar()
        super.onRefresh()
    }

    override fun onDestroy() {
        stopPeriodicWorker()
        stopSnackbar()
        super.onDestroy()
    }

    private fun startPeriodicWorker() {
        if (scheduledFuture != null) return

        // Here we're using ScheduledThreadPoolExecutor too run periodic background tasks
        // in each of which we run separate CoroutineWorker using Android WorkManager library
        val exec = ScheduledThreadPoolExecutor(1)
        val runnableCode = Runnable {
            runSilentRefreshChatsWorker()
        }
        scheduledFuture = exec.scheduleWithFixedDelay(
            runnableCode,
            0L, // first task runs immediately
            CHATS_PERIODIC_DELAY, // each next task runs after terminating of previous one with delay
            TimeUnit.MILLISECONDS
        )
    }

    private fun stopPeriodicWorker() {
        // cancel all pending tasks, but allowing to finish current working
        scheduledFuture?.cancel(false)
        scheduledFuture = null
    }

    private fun runSilentRefreshChatsWorker() {
        // building separate one time refresh chat worker
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val workRequest: WorkRequest = OneTimeWorkRequestBuilder<SilentRefreshChatsWorker>()
            .setConstraints(constraints)
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
                        val result = info.outputData.getString(CHATS_KEY)
                        viewModel.handleBackgroundChatsResult(result)
                    }
                })
        }
    }
}