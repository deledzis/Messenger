package com.deledzis.messenger.cache.db.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class Converters {

    @TypeConverter
    fun dateToString(item: Date): String {
        val df = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        return df.format(item)
    }

    @TypeConverter
    fun stringToDate(item: String): Date {
        val df = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
        return df.parse(item)!!
    }

    @TypeConverter
    fun listIntsToString(list: List<Int>): String? = Gson().toJson(list)

    @TypeConverter
    fun stringToListInts(string: String): List<Int> = Gson().fromJson(
        string,
        object : TypeToken<List<Int>>() {}.type
    )

    @TypeConverter
    fun stringToMapInts(string: String): Map<Int, Int> = Gson().fromJson(
        string,
        object : TypeToken<Map<Int, Int>>() {}.type
    )

    @TypeConverter
    fun mapIntToString(map: Map<Int, Int>): String = Gson().toJson(map)

    @TypeConverter
    fun stringToMapStrings(string: String): HashMap<String, String> = Gson().fromJson(
        string,
        object : TypeToken<HashMap<String, String>>() {}.type
    )

    @TypeConverter
    fun mapStringToString(map: HashMap<String, String>): String = Gson().toJson(map)
}