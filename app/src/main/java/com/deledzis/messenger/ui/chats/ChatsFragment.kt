package com.deledzis.messenger.ui.chats

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.databinding.FragmentChatsBinding
import com.deledzis.messenger.util.extensions.viewModelFactory
import com.google.android.material.snackbar.Snackbar

class ChatsFragment : BaseFragment(), ChatsActionsHandler {
    private lateinit var dataBinding: FragmentChatsBinding

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
    ): View? {
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

    override fun bindObservers() {
        viewModel.chats.observe(this, {

        })
        viewModel.error.observe(this, {
            val inputManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                HIDE_NOT_ALWAYS
            )
            Snackbar.make(dataBinding.root, it, Snackbar.LENGTH_INDEFINITE)
        })
    }

    override fun onAddChatClicked(view: View) {

    }

    override fun onSettingsClicked(view: View) {

    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatsFragment()
    }
}