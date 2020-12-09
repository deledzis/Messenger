package com.deledzis.messenger.util

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.text.Html
import android.util.Base64
import android.view.View
import android.widget.TextView

class Base64ImageGetter(
    private val context: Context,
    private val textView: TextView
) : Html.ImageGetter {

    override fun getDrawable(source: String): Drawable? {
        logv("Base64IG", "getDrawable img: $source")

        return try {
            val data = Base64.decode(source.substringAfter("data:image/png;base64,"), Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val d = BitmapDrawable(context.resources, bitmap)
            d.setBounds(0, 0, (textView.parent as View).width, (textView.parent as View).width / 2)
            d
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}