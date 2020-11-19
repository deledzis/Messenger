package com.deledzis.messenger.util

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

abstract class DeferredFragmentTransaction {

    @LayoutRes
    var containerId: Int = 0
    var fragment: Fragment? = null

    abstract fun commit()
}