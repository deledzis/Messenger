package com.deledzis.messenger.binding

import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.deledzis.messenger.util.Base64ImageGetter
import com.deledzis.messenger.util.DateUtils
import com.deledzis.messenger.util.GlideImageGetter
import java.text.SimpleDateFormat

@BindingAdapter(
    value = ["app:date", "app:inputDateFormat", "app:outputDateFormat"],
    requireAll = true
)
fun setDateFormat(
    view: TextView,
    date: String?,
    inputFormat: SimpleDateFormat,
    outputFormat: SimpleDateFormat
) {
    date?.let {
        if (it.isBlank()) return
        val dated = DateUtils.getDate(it, inputFormat) ?: return
        view.text = outputFormat.format(dated)
    }
}

@Suppress("DEPRECATION")
@BindingAdapter("from_html")
fun setTextFromHtml(view: TextView, value: String?) {
    value?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            view.text = Html.fromHtml(
                it,
                FROM_HTML_MODE_LEGACY,
                if (it.contains("data:image/png;base64"))
                    Base64ImageGetter(view.context, view)
                else
                    GlideImageGetter(view.context, view),
                null
            )
            view.movementMethod = LinkMovementMethod.getInstance()
        } else {
            view.text = Html.fromHtml(
                it,
                if (it.contains("data:image/png;base64"))
                    Base64ImageGetter(view.context, view)
                else
                    GlideImageGetter(view.context, view),
                null
            )
            view.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}