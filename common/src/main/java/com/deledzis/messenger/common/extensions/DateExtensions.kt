package com.deledzis.messenger.common.extensions

import com.deledzis.messenger.common.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.formatDate(
    format: SimpleDateFormat = DateUtils.ISO_24H_FORMAT,
    timeZone: String? = null
): String {
    format.timeZone =
        if (timeZone != null) TimeZone.getTimeZone("Europe/Moscow") else TimeZone.getDefault()
    return format.format(this)
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
    return today.after(this) && this.ignoreTime().daysBetween(today) == 1
}

fun Date.isInCurrentYear(): Boolean {
    val today = Calendar.getInstance()
    val thisDay = Calendar.getInstance().from(this)
    return thisDay.year == today.year
}

fun Date.month(): String {
    return this.formatDate(
        format = DateUtils.MONTH_FORMAT
    )
}

fun Date.hours(): Int {
    return this.formatDate(
        format = DateUtils.HOURS_FORMAT
    ).toIntOrNull() ?: 0
}

fun Date.minutes(): Int {
    return this.formatDate(
        format = DateUtils.MINUTES_FORMAT
    ).toIntOrNull() ?: 0
}

fun Date.daysBetween(other: Date): Int {
    return when {
        this.compareIgnoreTime(other) < 0 -> TimeUnit.DAYS.convert(
            other.time - this.time,
            TimeUnit.MILLISECONDS
        ).toInt()
        this.compareIgnoreTime(other) > 0 -> TimeUnit.DAYS.convert(
            this.time - other.time,
            TimeUnit.MILLISECONDS
        ).toInt()
        else -> 0
    }
}

fun Date.getBigger(other: Date): Date = if (this > other) this else other

fun Date.getLower(other: Date): Date = if (this < other) this else other

@Suppress("SimpleDateFormat")
fun Date.convertWithTimeZone(
    timePattern: String = "yyyy-MM-dd'T'HH:mm:ss",
    timeZone: String = "Europe/Moscow"
): Date? {
    val isoFormat = SimpleDateFormat(timePattern)
    isoFormat.timeZone = TimeZone.getTimeZone(timeZone)
    val oldDate = isoFormat.format(this)
    isoFormat.timeZone = TimeZone.getDefault()
    return isoFormat.parse(oldDate)
}

fun Date.formatForChat(): String = when {
    this.isToday() -> "Сегодня"
    this.isYesterday() -> "Вчера"
    this.isInCurrentYear() -> this.formatDate(format = DateUtils.DATE_FORMATTED)
    else -> this.formatDate(format = DateUtils.DATE_FORMATTED_FULL)
}