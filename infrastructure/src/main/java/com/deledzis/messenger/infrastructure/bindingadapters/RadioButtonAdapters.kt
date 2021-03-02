package com.deledzis.messenger.infrastructure.bindingadapters

import android.graphics.Typeface
import android.widget.RadioButton
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.deledzis.messenger.infrastructure.R

@BindingAdapter("android:customFont")
fun loadImage(view: RadioButton, custom: Boolean) {
    if (custom) {
        val font: Typeface? = ResourcesCompat.getFont(view.context, R.font.roboto_regular)
        view.typeface = font
    }
}