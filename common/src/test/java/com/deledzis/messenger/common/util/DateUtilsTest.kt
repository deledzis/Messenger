package com.deledzis.messenger.common.util

import com.deledzis.messenger.common.extensions.hours
import com.deledzis.messenger.common.extensions.minutes
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.text.SimpleDateFormat
import java.util.*

class DateUtilsTest {

    @Disabled
    @Test
    fun `getDate from year, month, day, ignoring time`() {
        val dateFromDateUtils = DateUtils.getDate(
            year = 2000,
            month = 5,
            day = 10,
            ignoreTime = true,
        )
//        println(dateFromDateUtils.time)
//        println(dateFromDateUtils.formatDate())
//        println(DateUtils.getDate("2000-06-10T00:00:00.000000")?.time)
//        Assertions.assertEquals(DateUtils.getDate("2000-06-10T00:00:00.000000")?.time, dateFromDateUtils.time)
//        Assertions.assertTrue(dateFromDateUtils.time - 960595200000 <= ONE_DAY_MILLIS)
        Assertions.assertEquals(960595200000, dateFromDateUtils.time)
    }

    @Test
    fun `getDate from year, month, day, considering time`() {
        val dateFromDateUtils = DateUtils.getDate(
            year = 2020,
            month = 7,
            day = 13,
            ignoreTime = false,
            timeZone = TimeZone.getTimeZone("Europe/Moscow")
        )
        Assertions.assertTrue(dateFromDateUtils.time - 1597266000000 <= ONE_DAY_MILLIS)
    }

    @Test
    fun `getDate from improperly formatted year, month and day does not throw`() {
        Assertions.assertDoesNotThrow {
            DateUtils.getDate(
                year = 1000000,
                month = 123123,
                day = 435345,
                ignoreTime = false,
                timeZone = TimeZone.getTimeZone("Europe/Moscow")
            )
        }
    }

    @Test
    fun `getDate with custom time properly formatted returns expected result`() {
        val date = DateUtils.getDate(
            hourOfDay = 10,
            minute = 20
        )
        Assertions.assertEquals(10, date.hours())
        Assertions.assertEquals(20, date.minutes())
    }

    @Test
    fun `getDate with custom time improperly formatted returns unexpected result`() {
        val date = DateUtils.getDate(
            hourOfDay = 130,
            minute = 500
        )
        Assertions.assertNotEquals(130, date.hours())
        Assertions.assertNotEquals(500, date.minutes())
    }

    @Test
    fun `getDate from string using dd MM yyyy format`() {
        val format = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        val stringWithDate = "26.03.2017"
        val dateFormattedByDateUtils = DateUtils.getDate(
            date = stringWithDate,
            format = format
        )
        val dateManuallyConstructed = DateUtils.getDate(
            year = 2017,
            month = 2,
            day = 26,
            ignoreTime = true
        )

        Assertions.assertEquals(dateManuallyConstructed, dateFormattedByDateUtils)
    }

    @Test
    fun `getDate returns null if string argument doesn't match format`() {
        val format = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        val stringWithDate = "13 15 2017"
        val dateFormattedByDateUtils = DateUtils.getDate(
            date = stringWithDate,
            format = format
        )

        Assertions.assertEquals(null, dateFormattedByDateUtils)
    }

    @Test
    fun `getCurrentDate considering time returns current date`() {
        val currentDate = Calendar.getInstance().time
        val currentDateFromDateUtils = DateUtils.getCurrentDate(ignoreTime = false)

        Assertions.assertTrue(currentDateFromDateUtils.time - currentDate.time <= SMALL_DELTA_MILLIS)
    }

    @Test
    fun `getCurrentDate ignoring time returns current date at 00-00`() {
        val currentDateFromDateUtils = DateUtils.getCurrentDate(ignoreTime = true)

        Assertions.assertEquals(0, currentDateFromDateUtils.hours())
        Assertions.assertEquals(0, currentDateFromDateUtils.minutes())
    }

    @Test
    fun `getCalendarFromDate returns correct Date`() {
        val currentDate = Date()
        val calendarFromDate = DateUtils.getCalendarFromDate(currentDate)

        Assertions.assertEquals(currentDate.time, calendarFromDate.time.time)
    }

    @Test
    fun `getDateWithOffset from current date ignoring time, adding seconds`() {
        val currentDate = DateUtils.getCurrentDate(ignoreTime = true)

        val currentDateWithOffset = DateUtils.getDateWithOffset(
            fromDate = currentDate,
            seconds = 37
        )

        Assertions.assertTrue(currentDateWithOffset.time - currentDate.time == 37 * ONE_SEC_MILLIS)
    }

    @Test
    fun `getDateWithOffset from current date considering time, subtracting minutes`() {
        val currentDate = Date()

        val currentDateWithOffset = DateUtils.getDateWithOffset(
            fromDate = currentDate,
            minutes = -15
        )

        Assertions.assertTrue(currentDate.time - currentDateWithOffset.time == 15 * ONE_MIN_MILLIS)
    }

    @Test
    fun `getDateWithOffset, adding 1 hour, not specifying fromDate`() {
        val currentDate = Date()

        val currentDateWithOffset = DateUtils.getDateWithOffset(hours = 1)

        Assertions.assertTrue(currentDateWithOffset.time - currentDate.time <= (ONE_HOUR_MILLIS + SMALL_DELTA_MILLIS))
    }

    @Test
    fun `getDateWithOffset from current date considering time, adding days`() {
        val currentDate = Date()

        val currentDateWithOffset = DateUtils.getDateWithOffset(
            fromDate = currentDate,
            days = 5
        )

        Assertions.assertTrue(currentDateWithOffset.time - currentDate.time == 5 * ONE_DAY_MILLIS)
    }

    @Test
    fun `getDateWithOffset from current date considering time, subtracting weeks`() {
        val currentDate = Date()

        val currentDateWithOffset = DateUtils.getDateWithOffset(
            fromDate = currentDate,
            weeks = -2
        )

        Assertions.assertTrue(currentDate.time - currentDateWithOffset.time == 14 * ONE_DAY_MILLIS)
    }

    @Test
    fun `getDateWithOffset from current date considering time, adding months`() {
        val calendar = Calendar.getInstance()
        val daysInCurrentMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val currentDate = calendar.time

        val currentDateWithOffset = DateUtils.getDateWithOffset(
            fromDate = currentDate,
            months = 1
        )

        Assertions.assertTrue(currentDateWithOffset.time - currentDate.time == daysInCurrentMonth * ONE_DAY_MILLIS)
    }

    companion object {
        private const val SMALL_DELTA_MILLIS: Long = 1000
        private const val ONE_SEC_MILLIS: Long = 1000
        private const val ONE_MIN_MILLIS: Long = 60 * ONE_SEC_MILLIS
        private const val ONE_HOUR_MILLIS: Long = 60 * ONE_MIN_MILLIS
        private const val ONE_DAY_MILLIS: Long = 24 * ONE_HOUR_MILLIS
    }
}