package com.deledzis.messenger.infrastructure.bindingadapters

import android.view.ViewGroup
import androidx.databinding.BindingAdapter

@BindingAdapter("android:alpha")
fun setViewGroupAlpha(view: ViewGroup, value: Boolean?) {
    value?.let {
        view.alpha = if (it) 1.0f else 0.6f
    }
}