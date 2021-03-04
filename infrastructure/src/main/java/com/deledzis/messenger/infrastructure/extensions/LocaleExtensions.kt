package com.deledzis.messenger.infrastructure.extensions

import android.icu.util.LocaleData
import android.icu.util.ULocale
import android.os.Build
import java.util.*

fun Locale.isMetric(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
    LocaleData.getMeasurementSystem(ULocale.forLocale(this)) == LocaleData.MeasurementSystem.SI
} else {
    when (country.toUpperCase(this)) {
        "US", "LR", "MM" -> false
        else -> true
    }
}