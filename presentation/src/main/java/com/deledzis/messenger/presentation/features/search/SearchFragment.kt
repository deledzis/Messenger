package com.deledzis.messenger.presentation.features.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseFragment
import com.deledzis.messenger.presentation.databinding.FragmentSearchBinding
import javax.inject.Inject

class SearchFragment @Inject constructor() :
    BaseFragment<SearchViewModel, FragmentSearchBinding>(layoutId = R.layout.fragment_search),
    SearchActionsHandler {

    override val viewModel: SearchViewModel by viewModels()
    private val args: SearchFragmentArgs by navArgs()

    @Inject
    lateinit var adapter: MessagesAdapter

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

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataBinding.rvMessages.layoutManager = LinearLayoutManager(activity)
        dataBinding.rvMessages.adapter = adapter
    }

    override fun bindObservers() {
        viewModel.messages.observe(viewLifecycleOwner, ::messagesObserver)
    }

    private fun messagesObserver(messages: List<Message>?) {
        adapter.messages = messages ?: return
        dataBinding.rvMessages.setItemViewCacheSize(messages.size)
    }

    override fun onBackClicked(view: View) {
        stopSnackbar()
        findNavController().popBackStack()
    }
}