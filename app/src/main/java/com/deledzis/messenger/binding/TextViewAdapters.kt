package com.deledzis.messenger.binding

import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.deledzis.messenger.R
import com.deledzis.messenger.data.model.chats.Message
import com.deledzis.messenger.util.Base64ImageGetter
import com.deledzis.messenger.util.DateUtils
import com.deledzis.messenger.util.GlideImageGetter
import com.deledzis.messenger.util.extensions.colorStateListFrom
import com.deledzis.messenger.util.extensions.formatDate
import com.deledzis.messenger.util.extensions.isToday
import com.deledzis.messenger.util.extensions.isYesterday
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

@BindingAdapter("app:chat_preview_content")
fun setChatPreviewContent(view: TextView, value: Boolean?) {
    value ?: return
    if (value) {
        view.setTypeface(null, Typeface.ITALIC)
        view.setTextColor(view.context.colorStateListFrom(R.color.text_outlined))
    } else {
        view.setTypeface(null, Typeface.NORMAL)
        view.setTextColor(view.context.colorStateListFrom(R.color.text))
    }
}

@BindingAdapter("app:message_date_preview")
fun setMessagePrevDate(view: TextView, value: String?) {
    value ?: return
    val date = DateUtils.getDate(value)
    view.text = when {
        date.isToday() -> date.formatDate(format = DateUtils.TIME_FORMAT)
        date.isYesterday() -> "вчера"
        else -> date.formatDate(format = DateUtils.RU_DATE_FORMAT)
    }
}

@BindingAdapter("app:message_text_preview", "app:user_id")
fun setMessagePreviewText(view: TextView, value: Message?, userId: Int) {
    value ?: return
    view.text = when (value.type) {
        true -> "Вложение"
        false -> if (value.author.id == userId) "Вы: ${value.content}" else value.content
    }
}