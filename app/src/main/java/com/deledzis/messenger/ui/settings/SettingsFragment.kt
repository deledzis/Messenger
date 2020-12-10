package com.deledzis.messenger.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.databinding.FragmentSettingsBinding
import com.deledzis.messenger.util.extensions.animateGone
import com.deledzis.messenger.util.extensions.animateShow
import com.deledzis.messenger.util.extensions.viewModelFactory

class SettingsFragment : BaseFragment(), SettingsActionsHandler {

    private lateinit var dataBinding: FragmentSettingsBinding

    private val viewModel: SettingsViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { SettingsViewModel() }
        )[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_settings,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.viewModel = viewModel
        dataBinding.controller = this

        return dataBinding.root
    }

    override fun bindObservers() {
        viewModel.userData.observe(viewLifecycleOwner, {
            hideKeyboard()
            if (it != null) {
                activity.setUserData(it)
                activity.navigateToHome()
            }
        })
        viewModel.loading.observe(viewLifecycleOwner, {
            it?.let { toggleUpdateProgress(progress = it) }
        })
        viewModel.error.observe(viewLifecycleOwner, {
            hideKeyboard()
            it?.let { Toast.makeText(activity, it, Toast.LENGTH_LONG).show() }
        })
        viewModel.usernameError.observe(viewLifecycleOwner, {
            hideKeyboard()
            dataBinding.tilUsername.error = it
        })
        viewModel.nicknameError.observe(viewLifecycleOwner, {
            hideKeyboard()
            dataBinding.tilNickname.error = it
        })
        viewModel.passwordError.observe(viewLifecycleOwner, {
            hideKeyboard()
            dataBinding.tilPassword.error = it
        })
        viewModel.newPasswordError.observe(viewLifecycleOwner, {
            hideKeyboard()
            dataBinding.tilNewPassword.error = it
        })
    }

    override fun onCancelClicked(view: View) {
        activity.removeFragment()
    }

    private fun toggleUpdateProgress(progress: Boolean) {
        if (progress) {
            dataBinding.icSave.animateGone()
            dataBinding.saveProgress.animateShow()
        } else {
            dataBinding.icSave.animateShow()
            dataBinding.saveProgress.animateGone()
        }
    }

    override fun onLogoutClicked(view: View) {
        activity.logout()
    }
}