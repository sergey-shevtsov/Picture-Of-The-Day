package com.sshevtsov.pictureoftheday.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*

fun Long.convertToNasaApiDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT-4")
    return dateFormat.format(this)
}

const val DARK_MODE_KEY = "DARK_MODE"
const val POD_DESCRIPTION_MODE_KEY = "POD_DESCRIPTION_MODE"
const val POD_HD_MODE_KEY = "POD_HD_MODE"

fun Activity.saveBooleanSettingInSharedPref(key: String, value: Boolean) {
    val sharedPref = this.getPreferences(Context.MODE_PRIVATE)
    sharedPref?.let {
        val editor = it.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}

fun Activity.getBooleanSettingFromSharedPref(key: String): Boolean {
    return this.getPreferences(Context.MODE_PRIVATE).getBoolean(key, false)
}

fun Context.hideKeyboard(view: View?) {
    val inputMethodManager = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}