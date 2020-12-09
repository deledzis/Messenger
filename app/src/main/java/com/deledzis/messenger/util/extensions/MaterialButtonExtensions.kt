package com.deledzis.messenger.util.extensions

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import com.deledzis.messenger.App
import com.google.android.material.button.MaterialButton

fun MaterialButton.animateShowing(@AnimRes animId: Int): MaterialButton {
    this.show()
    this.isClickable = false
    val slidingAnimation = AnimationUtils.loadAnimation(App.injector.context(), animId)
    slidingAnimation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationEnd(animation: Animation) {
            this@animateShowing.isClickable = true
        }

        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    })
    this.animation = slidingAnimation
    return this
}

fun MaterialButton.withTextColor(@ColorRes colorId: Int): MaterialButton {
    this.setTextColor(context.colorFrom(colorId))
    return this
}

fun MaterialButton.withBackgroundColor(@ColorRes colorId: Int?): MaterialButton {
    this.backgroundTintList = colorId?.let { context.colorStateListFrom(it) }
    return this
}

fun MaterialButton.withRippleColor(@ColorRes colorId: Int?): MaterialButton {
    this.rippleColor = colorId?.let { context.colorStateListFrom(it) }
    return this
}

fun MaterialButton.withIcon(
    @DrawableRes drawableId: Int?,
    iconPadding: Int? = null
): MaterialButton {
    this.icon = drawableId?.let { context.drawableCompatFrom(it) }
    iconPadding?.let { this.iconPadding = it }
    return this
}

fun MaterialButton.withClickable(clickable: Boolean): MaterialButton {
    this.isClickable = clickable
    return this
}

fun MaterialButton.withOnClickListener(listener: () -> Unit): MaterialButton {
    this.setOnClickListener { listener() }
    return this
}