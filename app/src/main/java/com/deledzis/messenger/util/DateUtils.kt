package com.deledzis.messenger.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    val ISO_24H_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale("ru"))
    val DF_ONLY_DAY = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    val RU_DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    val TIME_FORMAT = SimpleDateFormat("HH:mm", Locale("ru"))

    fun getDate(date: String, format: SimpleDateFormat = ISO_24H_FORMAT): Date =
        format.parse(date)
}