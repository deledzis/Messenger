package com.deledzis.messenger.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.deledzis.messenger.R
import com.deledzis.messenger.util.extensions.findSuitableParent
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar

class ErrorSnackbar(parent: ViewGroup, content: ErrorSnackbarView) :
    BaseTransientBottomBar<ErrorSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                R.color.transparent
            )
        )
        getView().elevation = 0.0f
        getView().setPadding(16, 16, 16, 16)
    }

    companion object {
        fun make(
            view: View,
            text: String,
            indefinite: Boolean = false,
            onCloseClick: (() -> Unit)?,
            onRetryClick: (() -> Unit)?
        ): ErrorSnackbar {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_error_snackbar,
                parent,
                false
            ) as ErrorSnackbarView
            customView.setErrorText(text)
            customView.setOnCloseClickListener(onCloseClick)
            customView.setOnRetryClickListener(onRetryClick)

            // We create and return our Snackbar
            val snackbar = ErrorSnackbar(parent, customView)
            snackbar.duration = if (indefinite) Snackbar.LENGTH_INDEFINITE else Snackbar.LENGTH_SHORT

            return snackbar
        }

    }
}