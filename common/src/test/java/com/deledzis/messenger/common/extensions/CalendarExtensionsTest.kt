package com.deledzis.messenger.common.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*

class CalendarExtensionsTest {

    @Test
    fun `calendar fromDate returns the same date`() {
        val calendar: Calendar = Calendar.getInstance()
        val date: Date = Date()
        val calendarFromDate = calendar.from(date)

        Assertions.assertEquals(date, calendarFromDate.time)
        Assertions.assertEquals(date.time, calendarFromDate.time.time)
    }

    @Test
    fun `calendar get year returns correct value`() {
        val calendar: Calendar = Calendar.getInstance()
        Assertions.assertEquals(2021, calendar.year)

        calendar.run { add(Calendar.YEAR, 3) }
        Assertions.assertEquals(2024, calendar.year)
    }

    @Test
    fun `calendar get month returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.MONTH, Calendar.MARCH)
            this
        }
        Assertions.assertEquals(Calendar.MARCH, calendar.month)

        calendar.run { add(Calendar.MONTH, 3) }
        Assertions.assertEquals(Calendar.JUNE, calendar.month)
    }

    @Test
    fun `calendar get day returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.DAY_OF_MONTH, 10)
            this
        }
        Assertions.assertEquals(10, calendar.day)

        calendar.run { add(Calendar.DAY_OF_MONTH, 5) }
        Assertions.assertEquals(15, calendar.day)
    }

    @Test
    fun `calendar get hour returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.HOUR_OF_DAY, 10)
            this
        }
        Assertions.assertEquals(10, calendar.hour)

        calendar.run { add(Calendar.HOUR_OF_DAY, 5) }
        Assertions.assertEquals(15, calendar.hour)
    }

    @Test
    fun `calendar get minute returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.MINUTE, 10)
            this
        }
        Assertions.assertEquals(10, calendar.minute)

        calendar.run { add(Calendar.MINUTE, 5) }
        Assertions.assertEquals(15, calendar.minute)
    }

    @Test
    fun `calendar get second returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.SECOND, 10)
            this
        }
        Assertions.assertEquals(10, calendar.second)

        calendar.run { add(Calendar.SECOND, 5) }
        Assertions.assertEquals(15, calendar.second)
    }

    @Test
    fun `calendar get millisecond returns correct value`() {
        val calendar: Calendar = Calendar.getInstance().run {
            set(Calendar.MILLISECOND, 10)
            this
        }
        Assertions.assertEquals(10, calendar.millisecond)

        calendar.run { add(Calendar.MILLISECOND, 5) }
        Assertions.assertEquals(15, calendar.millisecond)
    }
}