package com.deledzis.messenger.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.deledzis.messenger.ui.main.MainActivity
import com.deledzis.messenger.util.ErrorSnackbar
import com.deledzis.messenger.util.Loggable

abstract class BaseFragment : Fragment(), Loggable {
    protected lateinit var activity: MainActivity
    protected lateinit var fm: FragmentManager
    protected var snackbar: ErrorSnackbar? = null
    private lateinit var _view: View

    override fun onAttach(context: Context) {
        super.onAttach(context)

        activity = context as MainActivity
        fm = childFragmentManager
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _view = view
        bindObservers()
    }

    protected open fun bindObservers() {}

    protected fun startSnackbar(
        text: String,
        indefinite: Boolean = false,
        retryAction: (() -> Unit)?
    ) {
        if (snackbar == null) {
            snackbar = ErrorSnackbar.make(
                view = _view,
                text = text,
                indefinite = indefinite,
                onCloseClick = { stopSnackbar() },
                onRetryClick = retryAction
            ).also { it.show() }
        }
    }

    protected fun stopSnackbar() {
        snackbar?.dismiss()
        snackbar = null
    }
}