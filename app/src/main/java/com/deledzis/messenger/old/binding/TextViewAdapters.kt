package com.deledzis.messenger.old.binding

import android.graphics.Typeface
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.deledzis.messenger.R
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.old.util.Base64ImageGetter
import com.deledzis.messenger.old.util.DateUtils
import com.deledzis.messenger.old.util.GlideImageGetter
import com.deledzis.messenger.old.util.extensions.*
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
        val dated = DateUtils.getDate(it, inputFormat)
        view.text = outputFormat.format(dated)
    }
}

@Suppress("DEPRECATION")
@BindingAdapter("app:fromHtml")
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

@BindingAdapter("android:chat_previewContent")
fun setChatPreviewContent(view: TextView, value: Message?) {
    if (value?.type == false) {
        view.setTypeface(null, Typeface.NORMAL)
        view.setTextColor(view.context.colorStateListFrom(R.color.text))
    } else {
        view.setTypeface(null, Typeface.ITALIC)
        view.setTextColor(view.context.colorStateListFrom(R.color.text_outlined))
    }
}

@BindingAdapter("android:message_datePreview")
fun setMessagePreviewDate(view: TextView, value: String?) {
    value ?: return
    val date = DateUtils.getDate(value)
    view.text = when {
        date.isToday() -> date.formatDate(format = DateUtils.TIME_FORMAT)
        date.isYesterday() -> "вчера"
        date.isInCurrentYear() -> date.formatDate(format = DateUtils.DATE_FORMAT)
        else -> date.formatDate(format = DateUtils.DATE_FORMAT_FULL)
    }
}

@BindingAdapter("android:message_date")
fun setMessageDate(view: TextView, value: String?) {
    value ?: return
    val date = DateUtils.getDate(value)
    view.text = date.formatDate(format = DateUtils.TIME_FORMAT)
}

@BindingAdapter("android:message_textPreview", "android:userId")
fun setMessagePreviewText(view: TextView, value: Message?, userId: Int) {
    view.text = when (value?.type) {
        true -> "Вложение"
        false -> if (value.author.id == userId) "Вы: ${value.content}" else value.content
        else -> "Диалог не начат"
    }
}