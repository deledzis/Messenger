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
import timber.log.Timber
import javax.inject.Inject

class SettingsFragment @Inject constructor() :
    BaseFragment<SettingsViewModel, FragmentSettingsBinding>(layoutId = R.layout.fragment_settings),
    SettingsActionsHandler {

    override val viewModel: SettingsViewModel by viewModels()

    private var passwordInput: String? = null

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
        super.bindObservers()
        userViewModel.user.observe(viewLifecycleOwner, ::userObserver)
        viewModel.userData.observe(viewLifecycleOwner, ::updateStatusObserver)
        viewModel.password.observe(viewLifecycleOwner, ::passwordObserver)
        viewModel.loading.observe(viewLifecycleOwner, ::loadingObserver)
        viewModel.usernameError.observe(viewLifecycleOwner, ::usernameErrorObserver)
        viewModel.nicknameError.observe(viewLifecycleOwner, ::nicknameErrorObserver)
        viewModel.passwordError.observe(viewLifecycleOwner, ::passwordErrorObserver)
        viewModel.newPasswordError.observe(viewLifecycleOwner, ::newPasswordErrorObserver)
        viewModel.updateError.observe(viewLifecycleOwner, ::errorObserver)
        userViewModel.deleteAccountError.observe(viewLifecycleOwner, ::errorObserver)
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

    private fun passwordObserver(password: String?) {
        passwordInput = password
        dataBinding.icSave.alpha = if (password.isNullOrBlank()) 0.5f else 1.0f
    }

    private fun loadingObserver(loading: Boolean?) {
        loading?.let { toggleUpdateProgress(progress = it) }
    }

    private fun usernameErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilUsername.error = getErrorString(error)
    }

    private fun nicknameErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilNickname.error = getErrorString(error)
    }

    private fun passwordErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilPassword.error = getErrorString(error)
    }

    private fun newPasswordErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilNewPassword.error = getErrorString(error)
    }

    private fun toggleUpdateProgress(progress: Boolean) {
        if (progress) {
            dataBinding.icSave.animateGone()
            dataBinding.saveProgress.animateShow()
        } else {
            dataBinding.icSave.animateShow(
                toAlpha = if (passwordInput.isNullOrBlank()) 0.5f else 1.0f
            )
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

    override fun onDeleteAccountClicked(view: View) {
        requireContext().showDialog(
            messageId = R.string.dialog_delete_account,
            positiveBtnId = R.string.dialog_btn_delete,
            negativeBtnId = R.string.dialog_btn_cancel
        ) {
            userViewModel.deleteAccount()
        }
    }
}