package com.deledzis.messenger.common.extensions

import com.deledzis.messenger.common.extensions.RegexPatterns.EMAIL_PATTERN
import java.util.*
import kotlin.math.pow
import kotlin.math.roundToInt

operator fun Boolean.inc(): Boolean = !this

fun Int?.orZero(): Int = this ?: 0

fun Double?.orZero(): Double = this ?: 0.0

fun Float?.orZero(): Float = this ?: 0.0f

fun String?.orEmpty(): String = this ?: ""

fun Double.roundTo(numFractionDigits: Int): Double {
    val factor = 10.0.pow(numFractionDigits.toDouble())
    return (this * factor).roundToInt() / factor
}

fun String.isValidMail(): Boolean =
    this.trim().toLowerCase(locale = Locale.getDefault()).matches(EMAIL_PATTERN)

fun <T> MutableList<T>.toggle(item: T): Boolean = if (this.contains(item)) {
    this.remove(item)
} else {
    this.add(item)
}

operator fun <E> List<E>.times(i: Int): MutableList<E> {
    val list = this.toMutableList()
    repeat(i - 1) {
        list.addAll(this)
    }
    return list
}

object RegexPatterns {
    val EMAIL_PATTERN: Regex =
        Regex("(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])")
}