package com.deledzis.messenger.infrastructure.bindingadapters

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.deledzis.messenger.infrastructure.util.debounce
import kotlinx.coroutines.MainScope

@BindingAdapter("android:onClick")
fun setDebounceListener(view: View, onClickListener: View.OnClickListener) {
    val clickWithDebounce: (view: View) -> Unit = debounce(scope = MainScope()) {
        onClickListener.onClick(it)
    }
    view.setOnClickListener(clickWithDebounce)
}

@BindingAdapter("android:visibility")
fun <T> setVisibility(view: View, value: MutableLiveData<List<T>>?) {
    view.visibility = if (value?.value.isNullOrEmpty()) View.GONE else View.VISIBLE
}

@BindingAdapter("android:visibilityReverse")
fun <T> setReverseVisibility(view: View, value: MutableLiveData<List<T>>?) {
    view.visibility = if (value?.value.isNullOrEmpty()) View.VISIBLE else View.GONE
}

@BindingAdapter("android:visibility")
fun setVisibilityFromBoolean(view: View, value: MutableLiveData<Boolean>?) {
    view.visibility = if (value?.value == true) View.VISIBLE else View.GONE
}

@BindingAdapter("android:visibilityReverse")
fun setReverseVisibilityFromBoolean(view: View, value: MutableLiveData<Boolean>?) {
    view.visibility = if (value?.value == true) View.GONE else View.VISIBLE
}

@BindingAdapter("android:visibility")
fun setVisibility(view: View, value: Boolean?) {
    view.visibility = if (value == true) View.VISIBLE else View.GONE
}

@BindingAdapter("android:visibilityReverse")
fun setReverseVisibility(view: View, value: Boolean?) {
    view.visibility = if (value == true) View.GONE else View.VISIBLE
}

@BindingAdapter("android:visibilityInv")
fun setVisibilityInv(view: View, value: Boolean?) {
    view.visibility = if (value == true) View.VISIBLE else View.INVISIBLE
}

@BindingAdapter("android:layout_marginTop")
fun setLayoutMarginTop(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.topMargin = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("android:layout_marginBottom")
fun setLayoutMarginBottom(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.bottomMargin = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("android:layout_marginStart")
fun setLayoutMarginStart(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("android:layout_marginEnd")
fun setLayoutMarginEnd(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginEnd = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("android:reduceAlpha")
fun setAlpha(view: View, reduceAlpha: Boolean?) {
    view.alpha = if (reduceAlpha == true) 0.5f else 1.0f
}