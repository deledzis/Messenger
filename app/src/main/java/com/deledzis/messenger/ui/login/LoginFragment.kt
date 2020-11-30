package com.deledzis.messenger.ui.login

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.deledzis.messenger.App
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.databinding.FragmentLoginBinding
import com.deledzis.messenger.di.model.TokenInterceptor
import com.deledzis.messenger.di.model.UserData
import com.deledzis.messenger.ui.register.RegisterFragment
import com.deledzis.messenger.util.REGISTER_FRAGMENT_TAG
import com.deledzis.messenger.util.extensions.viewModelFactory
import javax.inject.Inject

class LoginFragment : BaseFragment(), LoginActionsHandler {
    private lateinit var dataBinding: FragmentLoginBinding

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    @Inject
    lateinit var userData: UserData

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
    ): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.viewModel = viewModel
        dataBinding.controller = this

        App.injector.inject(this)
        userData.auth = null

        return dataBinding.root
    }

    override fun bindObservers() {
        viewModel.userData.observe(this, Observer {
            val inputManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                HIDE_NOT_ALWAYS
            )
            if (it != null) {
                userData.auth = it
                tokenInterceptor.token = it.token ?: ""
                activity.apply {
                    // TODO toHome()
                }
            }
        })
        viewModel.error.observe(this, Observer {
            val inputManager = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                HIDE_NOT_ALWAYS
            )
            Toast.makeText(activity, it ?: "Неизвестная ошибка", Toast.LENGTH_LONG).show()
        })
    }

    override fun onRegisterClicked() {
        activity.setFragment(
            fragment = RegisterFragment.newInstance(),
            tag = REGISTER_FRAGMENT_TAG
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }
}