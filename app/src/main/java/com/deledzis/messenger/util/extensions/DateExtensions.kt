package com.deledzis.messenger.util.extensions

import android.annotation.SuppressLint
import com.deledzis.messenger.util.DateUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Date.formatDate(format: SimpleDateFormat = DateUtils.RU_DATE_FORMAT, timeZone: String? = null): String {
    val dt = format
    dt.timeZone = if (timeZone != null) TimeZone.getTimeZone("Europe/Moscow") else TimeZone.getDefault()
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

fun Date.month(): String {
    return this.formatDate(
        format = DateUtils.MONTH_FORMAT
    )
}

fun Date.daysBetween(other: Date): Long {
    return when {
        this.compareIgnoreTime(other) < 0 -> TimeUnit.DAYS.convert(other.time - this.time, TimeUnit.MILLISECONDS)
        this.compareIgnoreTime(other) > 0 -> TimeUnit.DAYS.convert(this.time - other.time, TimeUnit.MILLISECONDS)
        else -> 0L
    }
}

fun Date.getBigger(other: Date) = if (this > other) this else other

fun Date.getLower(other: Date) = if (this < other) this else other

fun Date.afterToday(): Boolean = this.after(DateUtils.getCurrentDate())

@SuppressLint("SimpleDateFormat")
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

fun Date.copy() = Date(time)