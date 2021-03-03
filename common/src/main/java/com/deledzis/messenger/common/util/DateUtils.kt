package com.deledzis.messenger.common.util

import com.deledzis.messenger.common.extensions.ignoreTime
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
    val MONTH_FORMAT: SimpleDateFormat = SimpleDateFormat("LLLL yyyy", Locale("ru"))
    val DAY_FORMAT: SimpleDateFormat = SimpleDateFormat("dd", Locale("ru"))
    val HOURS_FORMAT: SimpleDateFormat = SimpleDateFormat("HH", Locale("ru"))
    val MINUTES_FORMAT: SimpleDateFormat = SimpleDateFormat("mm", Locale("ru"))

    fun getDate(year: Int, month: Int, day: Int, ignoreTime: Boolean = true): Date {
        val date = Calendar.getInstance(TimeZone.getDefault()).apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, day)
        }.time
        return if (ignoreTime) date.ignoreTime() else date
    }

    fun getDate(hourOfDay: Int, minute: Int): Date {
        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }.time
    }

    fun getDate(date: String?, format: SimpleDateFormat = ISO_24H_FORMAT): Date? = try {
        format.parse(date)
    } catch (e: Exception) {
        null
    }

    fun getCurrentDate(ignoreTime: Boolean = true): Date = if (ignoreTime) {
        Calendar.getInstance().time.ignoreTime()
    } else {
        Calendar.getInstance().time
    }

    fun getCalendarFromDate(date: Date): Calendar = Calendar.getInstance().apply {
        time = date
    }

    fun getDateWithOffset(
        fromDate: Date? = null,
        seconds: Int = 0,
        minutes: Int = 0,
        hours: Int = 0,
        days: Int = 0,
        weeks: Int = 0,
        months: Int = 0,
        years: Int = 0
    ): Date {
        val date = Calendar.getInstance()
        fromDate?.let { date.time = it }

        return with(date) {
            add(Calendar.SECOND, seconds)
            add(Calendar.MINUTE, minutes)
            add(Calendar.HOUR_OF_DAY, hours)
            add(Calendar.DATE, days)
            add(Calendar.WEEK_OF_YEAR, weeks)
            add(Calendar.MONTH, months)
            add(Calendar.YEAR, years)
            time
        }
    }
}