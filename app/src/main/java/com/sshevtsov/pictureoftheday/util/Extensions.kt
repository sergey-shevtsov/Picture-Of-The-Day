package com.sshevtsov.pictureoftheday.util

import java.text.SimpleDateFormat
import java.util.*

fun Long.convertToNasaApiDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT-4")
    return dateFormat.format(this)
}
