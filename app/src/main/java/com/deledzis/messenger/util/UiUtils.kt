package com.deledzis.messenger.util

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.deledzis.messenger.App

class UiUtils {

    companion object {
        val debouncer = Debouncer(Handler(Looper.getMainLooper()))

        @JvmStatic
        fun getStatusBarHeight(): Int {
            var result = 0
            val resourceId = App.injector.context()
                .resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = App.injector.context()
                    .resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        @JvmStatic
        fun getNavBarHeight(): Int {
            val id = App.injector.context()
                .resources
                .getIdentifier("config_showNavigationBar", "bool", "android")
            if (id < 0 || !App.injector.context().resources.getBoolean(id)) {
                return 0
            }
            var result = 0
            val resourceId = App.injector.context()
                .resources
                .getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = App.injector.context()
                    .resources.getDimensionPixelSize(resourceId)
            }
            return result
        }
    }
}