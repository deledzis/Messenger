package com.deledzis.messenger.common.extensions

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class StdLibExtensionsTest {

    @Test
    fun inc() {
        val bool = false
        Assertions.assertTrue(bool.inc())
    }

    @Test
    fun roundTo() {
        val rawDouble = 5.123314
        val rounded = rawDouble.roundTo(3)
        Assertions.assertEquals(rounded, 5.123)
    }

    @Test
    fun isValidMail() {
        val testMail1 = "adadadsad"
        Assertions.assertFalse(testMail1.isValidMail())
        val testMail2 = "adada@ds.ad"
        Assertions.assertTrue(testMail2.isValidMail())
    }

    @Test
    fun toggle() {
        val list = mutableListOf(1, 2, 3, 4, 5)
        list.toggle(1)
        list.toggle(6)
        Assertions.assertArrayEquals(list.toTypedArray(), arrayOf(2, 3, 4, 5, 6))
    }

    @Test
    fun times() {
        val list = listOf(1, 2, 3)
        val newList = list.times(3)
        Assertions.assertEquals(newList.size, 9)
    }
}