package com.deledzis.messenger.common.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class CalendarExtensionsTest {

    @Test
    fun `calendar fromDate returns the same date`() {
        val calendar: Calendar = Calendar.getInstance()
        val date = Date()
        val calendarFromDate = calendar.from(date)
        Assertions.assertEquals(date, calendarFromDate.time)
        Assertions.assertEquals(date.time, calendarFromDate.time.time)
    }

    @Test
    fun `calendar get year returns correct value`() {
        val calendar: Calendar = Calendar.getInstance()
        Assertions.assertEquals(2021, calendar.year)
        calendar.year = 2024
        Assertions.assertEquals(2024, calendar.year)
    }

    @Test
    fun `calendar get month returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.MONTH, Calendar.MARCH)
            this
        }
        Assertions.assertEquals(Calendar.MARCH, calendar.month)
        calendar.month = Calendar.JUNE
        Assertions.assertEquals(Calendar.JUNE, calendar.month)
    }

    @Test
    fun `calendar get day returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.DAY_OF_MONTH, 10)
            this
        }
        Assertions.assertEquals(10, calendar.day)
        calendar.day = 15
        Assertions.assertEquals(15, calendar.day)
    }

    @Test
    fun `calendar get hour returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.HOUR_OF_DAY, 10)
            this
        }
        Assertions.assertEquals(10, calendar.hour)
        calendar.hour = 15
        Assertions.assertEquals(15, calendar.hour)
    }

    @Test
    fun `calendar get minute returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.MINUTE, 10)
            this
        }
        Assertions.assertEquals(10, calendar.minute)
        calendar.minute = 15
        Assertions.assertEquals(15, calendar.minute)
    }

    @Test
    fun `calendar get second returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.SECOND, 10)
            this
        }
        Assertions.assertEquals(10, calendar.second)
        calendar.second = 15
        Assertions.assertEquals(15, calendar.second)
    }

    @Test
    fun `calendar get millisecond returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.MILLISECOND, 10)
            this
        }
        Assertions.assertEquals(10, calendar.millisecond)
        calendar.millisecond = 15
        Assertions.assertEquals(15, calendar.millisecond)
    }
}