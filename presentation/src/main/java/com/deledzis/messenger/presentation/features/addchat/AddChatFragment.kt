package com.deledzis.messenger.presentation.features.addchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.deledzis.messenger.domain.model.entity.chats.Chat
import com.deledzis.messenger.domain.model.entity.user.User
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseFragment
import com.deledzis.messenger.presentation.databinding.FragmentAddChatBinding
import com.deledzis.messenger.presentation.features.chats.ChatsFragmentDirections
import timber.log.Timber
import javax.inject.Inject

class AddChatFragment @Inject constructor() :
    BaseFragment<AddChatViewModel, FragmentAddChatBinding>(layoutId = R.layout.fragment_add_chat),
    AddChatActionsHandler,
    UserItemActionsHandler {

    override val viewModel: AddChatViewModel by viewModels()
    private lateinit var adapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.init()
    }

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

        adapter = UsersAdapter(this)
        dataBinding.rvUsers.layoutManager = LinearLayoutManager(requireActivity())
        dataBinding.rvUsers.adapter = adapter
    }

    override fun bindObservers() {
        viewModel.searchTextDebounced.observe(viewLifecycleOwner, { Timber.v("search: $it") })
        viewModel.users.observe(viewLifecycleOwner, ::usersObserver)
        viewModel.addedChat.observe(viewLifecycleOwner, ::chatObserver)
    }

    private fun chatObserver(chat: Chat?) {
        if (chat != null) {
            val action = ChatsFragmentDirections.actionChatsFragmentToChatFragment(
                chatId = chat.id,
                chatTitle = chat.title
            )
            findNavController().navigate(action)
        } else {
            startSnackbar(
                text = getString(R.string.error_add_chat),
                indefinite = true,
                retryAction = { viewModel.retry() }
            )
        }
    }

    private fun usersObserver(users: List<User>?) {
        adapter.users = users ?: return
        dataBinding.rvUsers.setItemViewCacheSize(users.size)
    }

    override fun onBackClicked(view: View) {
        stopSnackbar()
        findNavController().popBackStack()
    }

    override fun onSelected(user: User) {
        stopSnackbar()
        viewModel.addChat(user)
    }
}