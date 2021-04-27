package com.deledzis.messenger.presentation.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.infrastructure.extensions.hideSoftKeyboard
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseFragment
import com.deledzis.messenger.presentation.databinding.FragmentLoginBinding
import com.deledzis.messenger.presentation.features.main.UserViewModel
import timber.log.Timber
import javax.inject.Inject

open class LoginFragment @Inject constructor() :
    BaseFragment<LoginViewModel, FragmentLoginBinding>(layoutId = R.layout.fragment_login),
    LoginActionsHandler {

    override val viewModel: LoginViewModel by viewModels()

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
        viewModel.passwordError.observe(viewLifecycleOwner, ::passwordErrorObserver)
        viewModel.loginError.observe(viewLifecycleOwner, ::errorObserver)
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

    private fun passwordErrorObserver(@StringRes error: Int?) {
        hideSoftKeyboard(dataBinding.root)
        dataBinding.tilPassword.error = getErrorString(error)
    }

    override fun onLoginClicked(view: View) {
        hideSoftKeyboard()
        viewModel.login()
    }

    override fun onRegisterClicked(view: View) {
        hideSoftKeyboard()
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }
}