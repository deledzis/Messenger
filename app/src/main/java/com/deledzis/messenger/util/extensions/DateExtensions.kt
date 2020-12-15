package com.deledzis.messenger.util.extensions

import com.deledzis.messenger.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.formatDate(
    format: SimpleDateFormat = DateUtils.ISO_24H_FORMAT,
    timeZone: String? = null
): String {
    val dt = format
    dt.timeZone =
        if (timeZone != null) TimeZone.getTimeZone("Europe/Moscow") else TimeZone.getDefault()
    return dt.format(this)
}

fun Date.ignoreTime(): Date {
    val cal = Calendar.getInstance()
    cal.time = this
    cal.set(Calendar.HOUR_OF_DAY, 0)
    cal.set(Calendar.MINUTE, 0)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)
    time = cal.timeInMillis
    return this
}

fun Date.compareIgnoreTime(other: Date): Int {
    val thisDate = Calendar.getInstance()
        .also { it.time = this }
    val otherDate = Calendar.getInstance()
        .also { it.time = other }
    return thisDate.compareIgnoreTime(otherDate)
}

fun Date.isToday(): Boolean {
    val today = Calendar.getInstance().time.ignoreTime()
    return this.compareIgnoreTime(today) == 0
}

fun Date.isYesterday(): Boolean {
    val today = Calendar.getInstance().time.ignoreTime()
    return today.after(this) && this.ignoreTime().daysBetween(today) == 1L
}

fun Date.daysBetween(other: Date): Long {
    return when {
        this.compareIgnoreTime(other) < 0 -> TimeUnit.DAYS.convert(
            other.ignoreTime().time - this.ignoreTime().time,
            TimeUnit.MILLISECONDS
        )
        this.compareIgnoreTime(other) > 0 -> TimeUnit.DAYS.convert(
            this.ignoreTime().time - other.ignoreTime().time,
            TimeUnit.MILLISECONDS
        )
        else -> 0L
    }
}