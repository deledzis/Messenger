package com.deledzis.messenger.old.ui.main

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import com.deledzis.messenger.R
import com.deledzis.messenger.databinding.ActivityMainBinding
import com.deledzis.messenger.old.App
import com.deledzis.messenger.old.base.BaseActivity
import com.deledzis.messenger.old.data.model.auth.AuthorizedUser
import com.deledzis.messenger.old.di.model.TokenInterceptor
import com.deledzis.messenger.old.di.model.UserData
import com.deledzis.messenger.old.ui.chats.ChatsFragment
import com.deledzis.messenger.old.ui.login.LoginFragment
import com.deledzis.messenger.old.util.CHATS_FRAGMENT_TAG
import com.deledzis.messenger.old.util.LOGIN_FRAGMENT_TAG
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var userData: UserData

    @Inject
    lateinit var tokenInterceptor: TokenInterceptor

    private lateinit var databinding: ActivityMainBinding
    private lateinit var storage: FirebaseStorage
    private lateinit var storageRef: StorageReference

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

        storage = Firebase.storage
        this.storageRef = storage.reference

        if (savedInstanceState == null) {
            if (userData.authorizedUser != null &&
                !userData.authorizedUser.accessToken.isNullOrBlank()
            ) {
                tokenInterceptor.token = userData.authorizedUser!!.accessToken!!
                navigateToHome()
            } else {
                navigateToLogin()
            }
        }
    }

    fun navigateToHome() {
        setFragment(
            fragment = ChatsFragment(),
            tag = CHATS_FRAGMENT_TAG
        )
    }

    private fun navigateToLogin() {
        setFragment(
            fragment = LoginFragment(),
            tag = LOGIN_FRAGMENT_TAG
        )
    }

    fun setUserData(user: AuthorizedUser?) {
        userData.authorizedUser = user
        tokenInterceptor.token = user?.accessToken ?: ""
    }

    fun logout() {
        setUserData(null)
        navigateToLogin()
    }

    fun getStorageRef() = storageRef

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
}
