package com.deledzis.messenger.ui.addchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.data.model.users.User
import com.deledzis.messenger.databinding.FragmentAddChatBinding
import com.deledzis.messenger.ui.chat.ChatFragment
import com.deledzis.messenger.util.CHAT_FRAGMENT_TAG
import com.deledzis.messenger.util.extensions.viewModelFactory

class AddChatFragment : BaseFragment(),
    AddChatActionsHandler,
    UserItemActionsHandler {

    private lateinit var dataBinding: FragmentAddChatBinding
    private lateinit var adapter: UsersAdapter

    private val viewModel: AddChatViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { AddChatViewModel() }
        )[AddChatViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_add_chat,
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
        adapter = UsersAdapter(this)
        dataBinding.rvUsers.layoutManager = LinearLayoutManager(activity)
        dataBinding.rvUsers.adapter = adapter

        viewModel.init(dataBinding.tieSearch)
    }

    override fun bindObservers() {
        viewModel.users.observe(viewLifecycleOwner, {
            Log.e("TAG", "Users: $it")
            adapter.users = it ?: return@observe
        })
        viewModel.addedChat.observe(viewLifecycleOwner, {
            Log.e("TAG", "Added chat: $it")
            if (it != null) {
                activity.replaceTopFragment(
                    fragment = ChatFragment(it),
                    tag = CHAT_FRAGMENT_TAG
                )
            } else {
                startSnackbar(
                    text = getString(R.string.error_add_chat),
                    indefinite = true,
                    retryAction = null
                )
            }
        })
    }

    override fun onBackClicked(view: View) {
        stopSnackbar()
        activity.removeFragment()
    }

    override fun onSelected(user: User) {
        stopSnackbar()
        viewModel.addChat(user)
    }
}