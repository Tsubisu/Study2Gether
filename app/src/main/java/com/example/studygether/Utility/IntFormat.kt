package com.example.studygether.Utility

import java.text.NumberFormat
import java.util.Locale

fun Int.formatWithCommas(): String {
    return NumberFormat.getNumberInstance(Locale.UK).format(this)
}