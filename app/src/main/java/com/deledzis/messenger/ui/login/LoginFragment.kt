package com.deledzis.messenger.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.databinding.FragmentLoginBinding
import com.deledzis.messenger.ui.register.RegisterFragment
import com.deledzis.messenger.util.REGISTER_FRAGMENT_TAG
import com.deledzis.messenger.util.extensions.viewModelFactory

class LoginFragment : BaseFragment(), LoginActionsHandler {
    private lateinit var dataBinding: FragmentLoginBinding

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { LoginViewModel() }
        )[LoginViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.viewModel = viewModel
        dataBinding.controller = this

        activity.setUserData(null)

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
        viewModel.error.observe(viewLifecycleOwner, {
            hideKeyboard()
            it?.let { Toast.makeText(activity, it, Toast.LENGTH_LONG).show() }
        })
        viewModel.usernameError.observe(viewLifecycleOwner, {
            hideKeyboard()
            dataBinding.tilUsername.error = it
        })
        viewModel.passwordError.observe(viewLifecycleOwner, {
            hideKeyboard()
            dataBinding.tilPassword.error = it
        })
    }

    override fun onRegisterClicked() {
        activity.setFragment(
            fragment = RegisterFragment(),
            tag = REGISTER_FRAGMENT_TAG
        )
    }
}