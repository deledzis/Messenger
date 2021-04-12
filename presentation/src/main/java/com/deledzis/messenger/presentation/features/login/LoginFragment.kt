package com.deledzis.messenger.presentation.features.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.deledzis.messenger.OpenForTesting
import com.deledzis.messenger.domain.model.entity.auth.Auth
import com.deledzis.messenger.infrastructure.extensions.hideSoftKeyboard
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseFragment
import com.deledzis.messenger.presentation.databinding.FragmentLoginBinding
import com.deledzis.messenger.presentation.features.main.UserViewModel
import timber.log.Timber
import javax.inject.Inject

@OpenForTesting
open class LoginFragment @Inject constructor() :
    BaseFragment<LoginViewModel, FragmentLoginBinding>(layoutId = R.layout.fragment_login),
    LoginActionsHandler {

    @Inject
    lateinit var userViewModel: UserViewModel

    override val viewModel: LoginViewModel by viewModels()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var navController: NavController? = null
        set(value) {
            Timber.e("Value: $value")
            field = value
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        Timber.e("ON CREATE VIEW")
        dataBinding.viewModel = viewModel
        dataBinding.controller = this

        return dataBinding.root
    }

    override fun bindObservers() {
        viewModel.user.observe(viewLifecycleOwner, ::userObserver)
        viewModel.loginError.observe(viewLifecycleOwner, ::errorObserver)
    }

    private fun userObserver(auth: Auth?) {
        if (auth != null) {
            userViewModel.saveUser(auth)
            findNavController().popBackStack()
        }
    }

    private fun errorObserver(@StringRes error: Int?) {
        hideSoftKeyboard()
        val errorMessage = getErrorString(error) ?: return
        Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onRegisterClicked(view: View) {
        Timber.e("HEREEE")
        hideSoftKeyboard()
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }
}