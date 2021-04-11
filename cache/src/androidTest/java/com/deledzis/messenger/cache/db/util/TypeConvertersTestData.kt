package com.deledzis.messenger.cache.db.util

import java.text.SimpleDateFormat
import java.util.*

class TypeConvertersTestData {
    companion object {
        val dateDate: Date = Date(1616360400000)
        val dateString = SimpleDateFormat("dd.MM.yyyy", Locale("ru")).format(dateDate)
        val listOfInts = listOf(1, 2, 3, 4, 5)
        val listOfIntsString = "[1,2,3,4,5]"
        val mapInts = mapOf<Int, Int>(1 to 2, 3 to 4, 5 to 6)
        val mapIntsString = "{\"1\":2,\"3\":4,\"5\":6}"
        val hashMap = hashMapOf<String, String>("ab" to "cd", "ef" to "gh", "ij" to "kl")
        val hashMapString = "{\"ab\":\"cd\",\"ef\":\"gh\",\"ij\":\"kl\"}"

    }
}