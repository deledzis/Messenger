package com.deledzis.messenger.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.deledzis.messenger.App
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.databinding.FragmentSearchBinding
import com.deledzis.messenger.util.extensions.viewModelFactory
import com.deledzis.messenger.util.logi

class SearchFragment : BaseFragment(), SearchActionsHandler {

    private lateinit var dataBinding: FragmentSearchBinding
    private lateinit var adapter: MessagesAdapter

    private val viewModel: SearchViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { SearchViewModel() }
        )[SearchViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_search,
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

        adapter = MessagesAdapter(App.injector.userData().authorizedUser?.id ?: 0)
        dataBinding.rvMessages.layoutManager = LinearLayoutManager(activity)
        dataBinding.rvMessages.adapter = adapter

        viewModel.init(dataBinding.tieSearch)
    }

    override fun bindObservers() {
        viewModel.messages.observe(viewLifecycleOwner, {
            logi { "Messages: $it" }
            adapter.messages = it ?: return@observe
        })
    }

    override fun onBackClicked(view: View) {
        stopSnackbar()
        activity.removeFragment()
    }
}