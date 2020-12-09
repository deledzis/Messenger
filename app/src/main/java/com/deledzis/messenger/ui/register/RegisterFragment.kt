package com.deledzis.messenger.ui.register

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.databinding.FragmentRegisterBinding
import com.deledzis.messenger.di.model.TokenInterceptor
import com.deledzis.messenger.di.model.UserData
import com.deledzis.messenger.ui.login.LoginFragment
import com.deledzis.messenger.util.LOGIN_FRAGMENT_TAG
import com.deledzis.messenger.util.extensions.viewModelFactory
import javax.inject.Inject

class RegisterFragment : BaseFragment(), RegisterActionsHandler {
    private lateinit var dataBinding: FragmentRegisterBinding

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    @Inject
    lateinit var userData: UserData

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
            val inputManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                HIDE_NOT_ALWAYS
            )
            if (it != null) {
                userData.authorizedUser = it
                tokenInterceptor.token = it.accessToken ?: ""
                activity.navigateToHome()
            }
        })
        viewModel.error.observe(viewLifecycleOwner, {
            val inputManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                HIDE_NOT_ALWAYS
            )
            Toast.makeText(activity, it ?: "Неизвестная ошибка", Toast.LENGTH_LONG).show()
        })
    }

    override fun onLoginClicked() {
        activity.setFragment(
            fragment = LoginFragment.newInstance(),
            tag = LOGIN_FRAGMENT_TAG
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = RegisterFragment()
    }
}