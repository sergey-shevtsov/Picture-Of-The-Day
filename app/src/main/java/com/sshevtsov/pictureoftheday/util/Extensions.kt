package com.sshevtsov.pictureoftheday.util

import android.app.Activity
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

fun Long.convertToNasaApiDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT-4")
    return dateFormat.format(this)
}

const val DARK_MODE_KEY = "darkModeKey"

fun Activity.saveDarkModeInSharedPref(value: Boolean) {
    val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
    sharedPref?.let {
        val editor = it.edit()
        editor.putBoolean(DARK_MODE_KEY, value)
        editor.apply()
    }
}

fun Activity.isDarkMode(): Boolean {
    return this.getPreferences(Context.MODE_PRIVATE).getBoolean(DARK_MODE_KEY, false)
}
