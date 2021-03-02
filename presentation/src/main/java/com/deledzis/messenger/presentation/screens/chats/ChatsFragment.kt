package com.deledzis.messenger.presentation.screens.chats

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deledzis.messenger.common.Constants
import com.deledzis.messenger.common.Constants.CHATS_PERIODIC_DELAY
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.domain.model.entity.user.BaseUserData
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseFragment
import com.deledzis.messenger.presentation.databinding.FragmentChatsBinding
import com.deledzis.messenger.presentation.screens.chats.SilentRefreshChatsWorker.Companion.CHATS_KEY
import timber.log.Timber
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatsFragment @Inject constructor() :
    BaseFragment<ChatsViewModel, FragmentChatsBinding>(layoutId = R.layout.fragment_chats),
    ChatsActionsHandler,
    ChatItemActionsHandler {

    override val viewModel: ChatsViewModel by viewModels()
    private lateinit var adapter: ChatsAdapter
    private var scheduledFuture: ScheduledFuture<*>? = null

    @Inject
    lateinit var userData: BaseUserData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        dataBinding.viewModel = viewModel
        dataBinding.controller = this

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ChatsAdapter(
            controller = this,
            userId = userData.getAuthUser()?.id ?: -1
        )
        dataBinding.rvChats.layoutManager = LinearLayoutManager(activity)
        dataBinding.rvChats.adapter = adapter

        srl = dataBinding.srl
        srl?.setOnRefreshListener(this)
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
        viewModel.getChats(refresh = srl?.isRefreshing ?: false)
        viewModel.chats.observe(viewLifecycleOwner, ::chatsObserver)
        viewModel.loadingError.observe(viewLifecycleOwner, ::errorObserver)
    }

    private fun chatsObserver(chats: List<Chat>?) {
        srl?.isRefreshing = false
        adapter.chats = chats ?: return
        dataBinding.rvChats.setItemViewCacheSize(chats.size)
    }

    private fun errorObserver(error: Boolean?) {
        srl?.isRefreshing = false
        error?.let {
            startSnackbar(
                text = getString(R.string.error_get_chats),
                indefinite = true
            ) { viewModel.getChats(refresh = true) }
        } ?: run { stopSnackbar() }
    }

    override fun onAddChatClicked(view: View) {
        stopSnackbar()
        val action = ChatsFragmentDirections.actionChatsFragmentToAddChatFragment()
        findNavController().navigate(action)
    }

    override fun onSettingsClicked(view: View) {
        stopSnackbar()
        val action = ChatsFragmentDirections.actionChatsFragmentToSettingsFragment()
        findNavController().navigate(action)
    }

    override fun onSelected(chat: Chat) {
        stopSnackbar()
        val action = ChatsFragmentDirections.actionChatsFragmentToChatFragment(chat)
        findNavController().navigate(action)
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
                .observe(this) { info ->
                    if (info != null && info.state.isFinished) {
                        Timber.i("Observed info: $info")
                        val result = info.outputData.getString(CHATS_KEY)
                        viewModel.handleBackgroundChatsResult(result)
                    }
                }
        }
    }
}