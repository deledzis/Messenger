package com.deledzis.messenger.common.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

internal class DateExtensionsTest {

    @Test
    fun compareIgnoreTime() {
        val date1 = Date(1616353800000L)
        val date2 = Date(1616353000000L)
        Assertions.assertTrue(date1.compareIgnoreTime(date2) == 0)
    }

    @Test
    fun isToday() {
        val date = Date()
        Assertions.assertTrue(date.isToday())
    }

    @Test
    fun isYesterday() {
        val date = Date()
        val yesterday = Date(date.time - 86400000L)
        Assertions.assertTrue(yesterday.isYesterday())
    }

    @Test
    fun isInCurrentYear() {
        val date = Date()
        Assertions.assertTrue(date.isInCurrentYear())
    }

    @Test
    fun minutes() {
        val date = Date(1616353800000L)
        val minutes = date.minutes()
        Assertions.assertEquals(minutes, 10)
    }

    @Test
    fun daysBetween() {
        val date1 = Date()
        val date2 = Date(date1.time - 3 * 86400000L)
        Assertions.assertEquals(date2.daysBetween(date1), 3)
    }

    @Test
    fun formatForChat() {
        val date = Date()
        val yesterday = Date(date.time - 86400000L)

        val dateFormatted = date.formatForChat()
        Assertions.assertEquals(dateFormatted, "Сегодня")
        val yesterdayFormatted = yesterday.formatForChat()
        Assertions.assertEquals(yesterdayFormatted, "Вчера")
    }
}