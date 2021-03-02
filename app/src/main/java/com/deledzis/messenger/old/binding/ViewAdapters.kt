package com.deledzis.messenger.old.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData

@BindingAdapter("android:visibility")
fun <T> setVisibility(view: View, value: MutableLiveData<List<T>>?) {
    view.visibility = if (value?.value.isNullOrEmpty()) {
        View.GONE
    } else View.VISIBLE
}

@BindingAdapter("android:visibilityReverse")
fun <T> setReverseVisibility(view: View, value: MutableLiveData<List<T>>?) {
    view.visibility = if (value?.value.isNullOrEmpty()) {
        View.VISIBLE
    } else View.GONE
}

@BindingAdapter("android:visibility")
fun setVisibilityFromBoolean(view: View, value: MutableLiveData<Boolean>?) {
    view.visibility = if (value?.value == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("android:visibilityReverse")
fun setReverseVisibilityFromBoolean(view: View, value: MutableLiveData<Boolean>?) {
    view.visibility = if (value?.value == true) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean?) {
    view.visibility = if (value == true) View.VISIBLE else View.GONE
}

@BindingAdapter("android:visibilityReverse")
fun setReverseVisibilityFromBoolean(view: View, value: Boolean?) {
    view.visibility = if (value == true) View.GONE else View.VISIBLE
}

@BindingAdapter("android:visibilityInv")
fun setVisibilityInv(view: View, value: Boolean?) {
    view.visibility = if (value == true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("android:reduceAlpha")
fun setAlpha(view: View, value: Boolean?) {
    value?.let {
        view.alpha = if (it) 1.0f else 0.5f
    }
}