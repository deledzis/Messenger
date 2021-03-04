package com.deledzis.messenger.infrastructure.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun View.show(): View {
    this.visibility = View.VISIBLE
    return this
}

fun View.hide(): View {
    this.visibility = View.GONE
    return this
}

fun View.invisible(): View {
    this.visibility = View.INVISIBLE
    return this
}

fun View.isVisible(): Boolean = this.visibility == View.VISIBLE

inline fun View.runWithDelay(delayMS: Long, crossinline run: View.() -> Unit) {
    GlobalScope.launch {
        delay(delayMS)
        this@runWithDelay.run()
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.animateShow() {
    animate().alpha(1.0f).setDuration(250)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@animateShow.show()
            }
        })
}

fun View.animateInvisible() {
    animate().alpha(0.0f).setDuration(250)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@animateInvisible.invisible()
            }
        })
}

fun View.animateGone() {
    animate().alpha(0.0f).setDuration(250)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                this@animateGone.hide()
            }
        })
}

fun View.toBitmap(height: Int, width: Int): Bitmap? {
    //Define a bitmap with the same size as the view
    val returnedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    //Bind a canvas to it
    val canvas = Canvas(returnedBitmap)
    //Get the view's background
    val bgDrawable = this.background
    if (bgDrawable != null) {
        //has background drawable, then draw it on the canvas
        bgDrawable.draw(canvas)
    } else {
        //does not have background drawable, then draw white background on the canvas
        canvas.drawColor(Color.WHITE)
    }
    // draw the view on the canvas
    this.draw(canvas)
    //return the bitmap
    return returnedBitmap
}

fun TextView.drawableStart(@DrawableRes drawableRes: Int) {
    this.setCompoundDrawablesWithIntrinsicBounds(drawableRes, 0, 0, 0)
}

fun TextView.clearDrawables() {
    this.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
}

internal fun View?.findSuitableParent(): ViewGroup? {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            // We've found a CoordinatorLayout, use it
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                // If we've hit the decor content view, then we didn't find a CoL in the
                // hierarchy, so use it.
                return view
            } else {
                // It's not the content view but we'll use it as our fallback
                fallback = view
            }
        }

        if (view != null) {
            // Else, we will loop and crawl up the view hierarchy and try to find a parent
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)

    // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
    return fallback
}