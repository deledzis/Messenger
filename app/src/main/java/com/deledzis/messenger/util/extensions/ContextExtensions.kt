package com.deledzis.messenger.util.extensions

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import com.deledzis.messenger.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.drawableCompatFrom(drawableId: Int): Drawable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        this.resources.getDrawable(drawableId, this.theme)
    } else {
        @Suppress("DEPRECATION")
        this.resources.getDrawable(drawableId)
    }
}

fun Context.colorFrom(colorId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.resources.getColor(colorId, this.theme)
    } else {
        @Suppress("DEPRECATION")
        this.resources.getColor(colorId)
    }
}

fun Context.colorStateListFrom(colorId: Int): ColorStateList {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.resources.getColorStateList(colorId, this.theme)
    } else {
        @Suppress("DEPRECATION")
        this.resources.getColorStateList(colorId)
    }
}

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Context.showDialog(
    @StringRes messageId: Int,
    @StringRes positiveBtnId: Int,
    @StringRes negativeBtnId: Int,
    onPositive: () -> Unit
) {
    val dialog = MaterialAlertDialogBuilder(this, R.style.AlertDialogTheme_Light)
        .setMessage(messageId)
        .setBackground(drawableCompatFrom(R.drawable.bgr_dialog))
        .setPositiveButton(positiveBtnId) { dialog, _ ->
            onPositive.invoke()
            dialog.dismiss()
        }
        .setNegativeButton(negativeBtnId) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
    dialog.show()
}