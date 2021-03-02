package com.deledzis.messenger.infrastructure.extensions

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import com.google.android.material.button.MaterialButton

fun MaterialButton.animateShowing(context: Context, @AnimRes animId: Int): MaterialButton {
    this.show()
    this.isClickable = false
    val slidingAnimation = AnimationUtils.loadAnimation(context, animId)
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