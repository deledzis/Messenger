package com.deledzis.messenger.util.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.ActionBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.deledzis.messenger.App
import io.reactivex.Completable
import io.reactivex.subjects.CompletableSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun View.toggleKeyboard(
    show: Boolean
) {
    val imm = App.injector.context()
        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (show) {
        imm.showSoftInput(this, 0)
    } else {
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}

fun View.show(): View {
    this.visibility = View.VISIBLE
    return this
}

fun View.invisible(): View {
    this.visibility = View.INVISIBLE
    return this
}

fun View.hide(): View {
    this.visibility = View.GONE
    return this
}

fun View.runWithDelay(delayMS: Long, run: View.() -> Unit) {
    GlobalScope.launch {
        delay(delayMS)
        this@runWithDelay.run()
    }
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, false)
}

fun View.expand(duration: Long): Completable {
    val animationSubject = CompletableSubject.create()
    return animationSubject.doOnSubscribe {
        val matchParentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
        val wrapContentMeasureSpec =
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        measure(matchParentMeasureSpec, wrapContentMeasureSpec)
        val targetHeight = measuredHeight

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        layoutParams.height = 1
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                layoutParams.height = if (interpolatedTime == 1.0F)
                    ActionBar.LayoutParams.WRAP_CONTENT
                else {
                    show()
                    (targetHeight * interpolatedTime).toInt()
                }
                requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = duration
        startAnimation(a)
    }
}

fun View.collapse(duration: Long): Completable {
    val animationSubject = CompletableSubject.create()
    return animationSubject.doOnSubscribe {
        val initialHeight = measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1.0F) {
                    hide()
                } else {
                    layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                    requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        a.duration = duration
        startAnimation(a)
    }
}

fun View.rotate(duration: Long): Completable {
    val animationSubject = CompletableSubject.create()
    return animationSubject.doOnSubscribe {
        val expand = rotation == 0.0F
        ViewCompat.animate(this)
            .rotation(if (expand) 180.0F else 0.0F)
            .setDuration(duration)
            .start()
    }
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