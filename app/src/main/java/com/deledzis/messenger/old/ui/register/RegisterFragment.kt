package com.deledzis.messenger.old.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.deledzis.messenger.R
import com.deledzis.messenger.databinding.FragmentRegisterBinding
import com.deledzis.messenger.old.base.BaseFragment
import com.deledzis.messenger.old.ui.login.LoginFragment
import com.deledzis.messenger.old.util.LOGIN_FRAGMENT_TAG
import com.deledzis.messenger.old.util.extensions.viewModelFactory

class RegisterFragment : BaseFragment(), RegisterActionsHandler {
    private lateinit var dataBinding: FragmentRegisterBinding

    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { RegisterViewModel() }
        )[RegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register,
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
    }

    override fun onLoginClicked() {
        activity.setFragment(
            fragment = LoginFragment(),
            tag = LOGIN_FRAGMENT_TAG
        )
    }
}