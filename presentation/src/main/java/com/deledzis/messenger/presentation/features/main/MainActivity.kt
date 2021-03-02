package com.deledzis.messenger.presentation.features.main

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowInsetsController
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.deledzis.messenger.infrastructure.extensions.createNotificationChannel
import com.deledzis.messenger.infrastructure.extensions.paintStatusBar
import com.deledzis.messenger.presentation.R
import com.deledzis.messenger.presentation.base.BaseActivity
import com.deledzis.messenger.presentation.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivity : BaseActivity<UserViewModel>() {

    private val dataBinding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
    }

    private val storage: FirebaseStorage by lazy { Firebase.storage }
    val storageRef: StorageReference by lazy { storage.reference }

    @Inject
    lateinit var userViewModel: UserViewModel

    @Inject
    lateinit var viewModel: MainActivityViewModel

    @SuppressLint("InlinedApi")
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Messenger)
        paintStatusBar(R.color.primary)

        if (Build.VERSION.SDK_INT in Build.VERSION_CODES.M until Build.VERSION_CODES.R) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
            val controller = window.insetsController
            controller?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        super.onCreate(savedInstanceState)

        if (!isTaskRoot) {
            finish()
            return
        }

        dataBinding.viewModel = viewModel
        dataBinding.userViewModel = userViewModel
        dataBinding.lifecycleOwner = this

        createNotificationsChannels()
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

    private fun createNotificationsChannels() {
        /* Default channel */
        createNotificationChannel(
            channelId = R.string.default_notification_channel_id,
            channelName = R.string.default_notification_channel_name,
            channelDescription = R.string.default_notification_channel_description
        )
    }
}