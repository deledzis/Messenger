package com.deledzis.messenger.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.deledzis.messenger.ui.main.MainActivity
import com.deledzis.messenger.util.ErrorSnackbar
import com.deledzis.messenger.util.Loggable
import com.deledzis.messenger.util.logv

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
        indefinite: Boolean = true,
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

    protected fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                logv { "Permission is granted" }
                true
            } else {
                logv { "Permission is revoked" }
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
                false
            }
        } else { // permission is automatically granted on sdk < 23 upon installation
            logv { "Permission is granted" }
            true
        }
    }

    protected fun hideKeyboard() {
        val inputManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            activity.currentFocus?.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}