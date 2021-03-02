package com.deledzis.messenger.presentation.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.infrastructure.extensions.animateGone
import com.deledzis.messenger.infrastructure.extensions.animateShow
import com.deledzis.messenger.infrastructure.extensions.hideSoftKeyboard
import com.deledzis.messenger.infrastructure.extensions.showDialog
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseFragment
import com.deledzis.messenger.presentation.databinding.FragmentSettingsBinding
import com.deledzis.messenger.presentation.features.main.UserViewModel
import javax.inject.Inject

class SettingsFragment @Inject constructor() :
    BaseFragment<SettingsViewModel, FragmentSettingsBinding>(layoutId = R.layout.fragment_settings),
    SettingsActionsHandler {

    override val viewModel: SettingsViewModel by viewModels()

    @Inject
    lateinit var userViewModel: UserViewModel

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

    override fun bindObservers() {
        userViewModel.user.observe(viewLifecycleOwner, ::userObserver)
        viewModel.userData.observe(viewLifecycleOwner, ::updateStatusObserver)
        viewModel.loading.observe(viewLifecycleOwner, ::loadingObserver)
        viewModel.error.observe(viewLifecycleOwner, ::errorObserver)
        viewModel.usernameError.observe(viewLifecycleOwner, ::usernameErrorObserver)
        viewModel.nicknameError.observe(viewLifecycleOwner, ::nicknameErrorObserver)
        viewModel.passwordError.observe(viewLifecycleOwner, ::passwordErrorObserver)
        viewModel.newPasswordError.observe(viewLifecycleOwner, ::newPasswordErrorObserver)
    }

    private fun userObserver(auth: Auth?) {
        if (auth == null) {
            findNavController().popBackStack()
        }
    }

    private fun updateStatusObserver(auth: Auth?) {
        userViewModel.saveUser(auth)
        if (auth == null) {
            findNavController().popBackStack()
        }
    }

    private fun loadingObserver(loading: Boolean?) {
        loading?.let { toggleUpdateProgress(progress = it) }
    }

    private fun errorObserver(error: String?) {
        hideSoftKeyboard(dataBinding.root)
        error?.let { Toast.makeText(activity, it, Toast.LENGTH_LONG).show() }
    }

    private fun usernameErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilUsername.error = error?.let { getString(it) }
    }

    private fun nicknameErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilNickname.error = error?.let { getString(it) }
    }

    private fun passwordErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilPassword.error = error?.let { getString(it) }
    }

    private fun newPasswordErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilNewPassword.error = error?.let { getString(it) }
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

    override fun onCancelClicked(view: View) {
        findNavController().popBackStack()
    }

    override fun onLogoutClicked(view: View) {
        requireContext().showDialog(
            messageId = R.string.dialog_logout,
            positiveBtnId = R.string.dialog_btn_exit,
            negativeBtnId = R.string.dialog_btn_cancel
        ) {
            userViewModel.handleLogout()
        }
    }
}