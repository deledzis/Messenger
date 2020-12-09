package com.deledzis.messenger.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseFragment
import com.deledzis.messenger.databinding.FragmentSettingsBinding
import com.deledzis.messenger.di.model.TokenInterceptor
import com.deledzis.messenger.di.model.UserData
import com.deledzis.messenger.util.extensions.viewModelFactory
import javax.inject.Inject

class SettingsFragment : BaseFragment(), SettingsActionsHandler {

    private lateinit var dataBinding: FragmentSettingsBinding

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    @Inject
    lateinit var userData: UserData

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

//        App.injector.inject(this)

        return dataBinding.root
    }

    override fun bindObservers() {
        viewModel.userData.observe(viewLifecycleOwner, {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            if (it != null) {
                userData.authorizedUser = it
                tokenInterceptor.token = it.accessToken ?: ""
                activity.navigateToHome()
            }
        })

        viewModel.error.observe(viewLifecycleOwner, {
            val inputManager =
                activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                activity.currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
            Toast.makeText(activity, it ?: "Неизвестная ошибка", Toast.LENGTH_LONG).show()
        })
    }

    override fun onCancelClicked(view: View) {
        activity.removeFragment()
    }

    override fun onLogoutClicked(view: View) {
        activity.logout()
    }
}