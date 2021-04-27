package com.deledzis.messenger.presentation.features.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.infrastructure.extensions.hideSoftKeyboard
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseFragment
import com.deledzis.messenger.presentation.databinding.FragmentRegisterBinding
import com.deledzis.messenger.presentation.features.main.UserViewModel
import javax.inject.Inject

class RegisterFragment @Inject constructor() :
    BaseFragment<RegisterViewModel, FragmentRegisterBinding>(layoutId = R.layout.fragment_register),
    RegisterActionsHandler {

    override val viewModel: RegisterViewModel by viewModels()

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
        viewModel.user.observe(viewLifecycleOwner, ::userObserver)
        viewModel.usernameError.observe(viewLifecycleOwner, ::usernameErrorObserver)
        viewModel.nicknameError.observe(viewLifecycleOwner, ::nicknameErrorObserver)
        viewModel.passwordError.observe(viewLifecycleOwner, ::passwordErrorObserver)
        viewModel.registerError.observe(viewLifecycleOwner, ::errorObserver)
    }

    private fun userObserver(auth: Auth?) {
        if (auth != null) {
            userViewModel.saveUser(auth)
            findNavController().popBackStack()
        }
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

    override fun onRegisterClicked(view: View) {
        hideSoftKeyboard()
        viewModel.register()
    }

    override fun onLoginClicked(view: View) {
        hideSoftKeyboard()
        val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        findNavController().navigate(action)
    }
}