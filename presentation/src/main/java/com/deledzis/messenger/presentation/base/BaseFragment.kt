package com.deledzis.messenger.presentation.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.deledzis.messenger.infrastructure.view.ErrorSnackbar
import com.deledzis.messenger.presentation.features.main.UserViewModel
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment<out T : ViewModel, B : ViewDataBinding>(
    @LayoutRes protected val layoutId: Int
) : Fragment(),
    LifecycleOwner,
    SwipeRefreshLayout.OnRefreshListener {

    protected lateinit var dataBinding: B
    protected abstract val viewModel: T
    protected open var srl: SwipeRefreshLayout? = null
    protected var snackbar: ErrorSnackbar? = null

    @Inject
    lateinit var assistedViewModelFactory: dagger.Lazy<InjectingSavedStateViewModelFactory>

    override fun getDefaultViewModelProviderFactory(): ViewModelProvider.Factory =
        assistedViewModelFactory.get().create(this, arguments)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("onAttach: $this")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate: $this")
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        dataBinding = DataBindingUtil.inflate(
            inflater,
            layoutId,
            container,
            false
        )
        dataBinding.lifecycleOwner = viewLifecycleOwner
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("onViewCreated: $this")
        bindObservers()
    }

    protected open fun bindObservers() {}

    protected fun authErrorObserver(authError: Boolean?, userViewModel: UserViewModel) {
        if (authError == true) {
            userViewModel.handleLogout()
        }
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart: $this")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop: $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("onDestroy: $this")
    }

    override fun onRefresh() {
        bindObservers()
    }

    private fun getStatusBarColor(@ColorRes statusBarColor: Int): Int {
        return statusBarColor
    }

    protected fun isStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Timber.v("Permission is granted")
                true
            } else {
                Timber.v("Permission is revoked")
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    EXTERNAL_STORAGE_PERMISSIONS_REQUEST_CODE
                )
                false
            }
        } else {
            // permission is automatically granted on sdk < 23 upon installation
            Timber.v("Permission is granted")
            true
        }
    }

    protected fun isCameraPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Timber.v("Permission is granted")
                true
            } else {
                Timber.v("Permission is revoked")
                requestPermissions(
                    arrayOf(Manifest.permission.CAMERA),
                    CAMERA_PERMISSIONS_REQUEST_CODE
                )
                false
            }
        } else {
            // permission is automatically granted on sdk < 23 upon installation
            Timber.v("Permission is granted")
            true
        }
    }

    protected fun Array<out String>.allPermissionsGranted(): Boolean = this.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    protected fun startSnackbar(
        text: String,
        indefinite: Boolean = true,
        retryAction: (() -> Unit)?
    ) {
        if (snackbar == null) {
            snackbar = ErrorSnackbar.make(
                view = dataBinding.root,
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

    companion object {
        const val EXTERNAL_STORAGE_PERMISSIONS_REQUEST_CODE: Int = 1235
        const val CAMERA_PERMISSIONS_REQUEST_CODE: Int = 1236

        fun Fragment.getErrorString(@StringRes error: Int?): String? =
            if (error != null && error != 0) this.getString(error) else null
    }
}