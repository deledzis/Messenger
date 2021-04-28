package com.deledzis.messenger.cache.db.util

import org.junit.Assert.assertEquals
import org.junit.Test

class TypeConvertersTest {

    @Test
    fun stringToListIntsTest() {
        val list = Converters().stringToListInts(TypeConvertersTestData.listOfIntsString)
        assertEquals(TypeConvertersTestData.listOfInts[0], list[0])
        assertEquals(TypeConvertersTestData.listOfInts[1], list[1])
        assertEquals(TypeConvertersTestData.listOfInts[2], list[2])
        assertEquals(TypeConvertersTestData.listOfInts[3], list[3])
        assertEquals(TypeConvertersTestData.listOfInts[4], list[4])
    }

    @Test
    fun listIntsToStringTest() {
        val listString = Converters().listIntsToString(TypeConvertersTestData.listOfInts)
        assertEquals(TypeConvertersTestData.listOfIntsString, listString)
    }

    @Test
    fun dateToStringTest() {
        val dateString = Converters().dateToString(TypeConvertersTestData.dateDate)
        assertEquals(TypeConvertersTestData.dateString, dateString)
    }

    @Test
    fun stringToDateTest() {
        val date = Converters().stringToDate(TypeConvertersTestData.dateString)
        assertEquals(TypeConvertersTestData.dateDate.time, date.time)
        assertEquals(TypeConvertersTestData.dateDate, date)
    }

    @Test
    fun stringToMapIntsTest() {
        val mapInts = Converters().stringToMapInts(TypeConvertersTestData.mapIntsString)
        assertEquals(TypeConvertersTestData.mapInts.size, mapInts.size)
        assertEquals(TypeConvertersTestData.mapInts.keys, mapInts.keys)
        assertEquals(TypeConvertersTestData.mapInts[1], mapInts[1])
        assertEquals(TypeConvertersTestData.mapInts[3], mapInts[3])
        assertEquals(TypeConvertersTestData.mapInts[5], mapInts[5])
    }

    @Test
    fun mapIntToStringTest() {
        val mapIntsString = Converters().mapIntToString(TypeConvertersTestData.mapInts)
        assertEquals(TypeConvertersTestData.mapIntsString, mapIntsString)
    }

    @Test
    fun stringToMapStringsTest() {
        val hashMap = Converters().stringToMapStrings(TypeConvertersTestData.hashMapString)
        assertEquals(TypeConvertersTestData.hashMap.size, hashMap.size)
        assertEquals(TypeConvertersTestData.hashMap.keys, hashMap.keys)
        assertEquals(TypeConvertersTestData.hashMap["ab"], hashMap["ab"])
        assertEquals(TypeConvertersTestData.hashMap["ef"], hashMap["ef"])
        assertEquals(TypeConvertersTestData.hashMap["ij"], hashMap["ij"])
    }

    @Test
    fun mapStringToStringTest() {
        val hashMapString = Converters().mapStringToString(TypeConvertersTestData.hashMap)
        assertEquals(TypeConvertersTestData.hashMapString, hashMapString)
    }
}