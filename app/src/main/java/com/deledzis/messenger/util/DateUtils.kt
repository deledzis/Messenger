package com.deledzis.messenger.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    val ISO_24H_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale("ru")).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }
    val DATE_FORMATTED = SimpleDateFormat("dd MMMM", Locale("ru"))
    val DATE_FORMATTED_FULL = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    val DATE_FORMAT = SimpleDateFormat("dd.MM", Locale("ru"))
    val DATE_FORMAT_FULL = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    val TIME_FORMAT = SimpleDateFormat("HH:mm", Locale("ru"))

    fun getDate(date: String, format: SimpleDateFormat = ISO_24H_FORMAT): Date =
        format.parse(date)
}