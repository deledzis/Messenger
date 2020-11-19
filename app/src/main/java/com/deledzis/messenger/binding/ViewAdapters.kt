package com.deledzis.messenger.binding

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData

@BindingAdapter("visibility")
fun <T> setVisibility(view: View, value: MutableLiveData<List<T>>?) {
    view.visibility = if (value?.value.isNullOrEmpty()) {
        View.GONE
    } else View.VISIBLE
}

@BindingAdapter("reverse_visibility")
fun <T> setReverseVisibility(view: View, value: MutableLiveData<List<T>>?) {
    view.visibility = if (value?.value.isNullOrEmpty()) {
        View.VISIBLE
    } else View.GONE
}

@BindingAdapter("visibility")
fun setVisibilityFromBoolean(view: View, value: MutableLiveData<Boolean>?) {
    view.visibility = if (value?.value == true) {
        View.VISIBLE
    } else {
        View.GONE
    }
}

@BindingAdapter("reverse_visibility")
fun setReverseVisibilityFromBoolean(view: View, value: MutableLiveData<Boolean>?) {
    view.visibility = if (value?.value == true) {
        View.GONE
    } else {
        View.VISIBLE
    }
}

@BindingAdapter("visibility")
fun setVisibility(view: View, value: Boolean?) {
    view.visibility = if (value == true) View.VISIBLE else View.GONE
}

@BindingAdapter("visibility_inv")
fun setVisibilityInv(view: View, value: Boolean?) {
    view.visibility = if (value == true) View.VISIBLE else View.INVISIBLE
}