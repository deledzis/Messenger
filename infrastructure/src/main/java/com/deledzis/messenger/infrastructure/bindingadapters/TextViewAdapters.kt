package com.deledzis.messenger.infrastructure.bindingadapters

import android.graphics.Typeface
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat
import androidx.databinding.BindingAdapter
import com.deledzis.messenger.common.extensions.formatDate
import com.deledzis.messenger.common.extensions.isInCurrentYear
import com.deledzis.messenger.common.extensions.isToday
import com.deledzis.messenger.common.extensions.isYesterday
import com.deledzis.messenger.common.util.DateUtils
import com.deledzis.messenger.domain.model.entity.messages.Message
import com.deledzis.messenger.infrastructure.R
import com.deledzis.messenger.infrastructure.extensions.colorStateListFrom
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("android:asyncText")
fun asyncText(view: TextView, text: CharSequence) {
    val params = TextViewCompat.getTextMetricsParams(view)
    (view as AppCompatTextView).setTextFuture(
        PrecomputedTextCompat.getTextFuture(text, params, null)
    )
}

@BindingAdapter(
    value = ["android:date", "android:outputDateFormat"],
    requireAll = true
)
fun setDateFormat(
    view: TextView,
    date: Date?,
    outputFormat: SimpleDateFormat
) {
    view.text = date?.let { outputFormat.format(it) } ?: "-"
//    asyncText(view, text)
}

@BindingAdapter("android:textRes")
fun setTextFromRes(view: TextView, @StringRes res: Int?) {
    res?.let {
        if (it > 0) {
            view.text = view.context.getString(it)
//            asyncText(view, text)
        }
    }
}

@BindingAdapter("android:chat_previewContent")
fun setChatPreviewContent(view: TextView, value: Message?) {
    if (value?.type == Message.TYPE_TEXT) {
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
        date?.isToday() == true -> date.formatDate(format = DateUtils.TIME_FORMAT)
        date?.isYesterday() == true -> "вчера"
        date?.isInCurrentYear() == true -> date.formatDate(format = DateUtils.DATE_FORMAT)
        else -> date?.formatDate(format = DateUtils.DATE_FORMAT_FULL)
    }
}

@BindingAdapter("android:message_date")
fun setMessageDate(view: TextView, value: String?) {
    value ?: return
    val date = DateUtils.getDate(value)
    view.text = date?.formatDate(format = DateUtils.TIME_FORMAT)
}

@BindingAdapter("android:message_textPreview", "android:userId")
fun setMessagePreviewText(view: TextView, value: Message?, userId: Int) {
    view.text = when (value?.type) {
        Message.TYPE_FILE -> "Вложение"
        Message.TYPE_IMAGE -> "Изображение"
        Message.TYPE_TEXT -> if (value.authorId == userId) "Вы: ${value.content}" else value.content
        else -> "Диалог не начат"
    }
}