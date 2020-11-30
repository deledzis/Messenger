package com.deledzis.messenger.ui.main

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.deledzis.messenger.App
import com.deledzis.messenger.R
import com.deledzis.messenger.base.BaseActivity
import com.deledzis.messenger.databinding.ActivityMainBinding
import com.deledzis.messenger.di.model.TokenInterceptor
import com.deledzis.messenger.di.model.UserData
import com.deledzis.messenger.ui.login.LoginFragment
import com.deledzis.messenger.ui.main.viewmodel.MainViewModel
import com.deledzis.messenger.util.LOGIN_FRAGMENT_TAG
import com.deledzis.messenger.util.extensions.viewModelFactory
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var userData: UserData

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    private lateinit var databinding: ActivityMainBinding

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(
            this,
            viewModelFactory { MainViewModel(userData) }
        )[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Messenger)

        super.onCreate(savedInstanceState)

        if (!isTaskRoot) {
            finish()
            return
        }

        App.injector.inject(this)

        databinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        databinding.lifecycleOwner = this

        if (savedInstanceState == null) {
            if (userData.auth != null) {
                start()
                bindObservers()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun start() {
        // TODO init vm
    }

    private fun bindObservers() {

    }

    private fun navigateToLogin() {
        setFragment(
            fragment = LoginFragment.newInstance(),
            tag = LOGIN_FRAGMENT_TAG
        )
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model
        } else {
            "$manufacturer $model"
        }
    }
}
