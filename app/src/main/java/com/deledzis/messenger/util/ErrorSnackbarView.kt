package com.deledzis.messenger.util

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import com.deledzis.messenger.R
import com.deledzis.messenger.databinding.ViewErrorSnackbarBinding
import com.google.android.material.snackbar.ContentViewCallback

class ErrorSnackbarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ContentViewCallback {

    private var binding: ViewErrorSnackbarBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.view_error_snackbar,
            this,
            true
        )
        clipToPadding = true
    }

    fun setErrorText(text: String) {
        binding.message.text = text
    }

    fun setOnCloseClickListener(onClick: () -> Unit) = binding.icClose.setOnClickListener {
        onClick()
    }

    fun setOnRetryClickListener(onClick: () -> Unit) = binding.btnRetry.setOnClickListener {
        onClick()
    }

    override fun animateContentIn(delay: Int, duration: Int) {
        /*val scaleX = ObjectAnimator.ofFloat(binding.root, View.SCALE_X, 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(binding.root, View.SCALE_Y, 0f, 1f)
        val animatorSet = AnimatorSet().apply {
            interpolator = OvershootInterpolator()
            setDuration(500)
            playTogether(scaleX, scaleY)
        }
        animatorSet.start()*/
    }

    override fun animateContentOut(delay: Int, duration: Int) {

    }
}
