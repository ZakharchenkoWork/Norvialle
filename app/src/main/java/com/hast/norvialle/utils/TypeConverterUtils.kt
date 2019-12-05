package com.hast.norvialle.utils

import android.widget.TextView

/**
 * Created by Konstantyn Zakharchenko on 02.12.2019.
 */
fun getIntValue(view: TextView): Int {
    var value = 0
    try {
        value = view.text.toString().toInt()
    } catch (nfe: NumberFormatException) {
        value = 0
    }
    return value
}

fun getIntValue(text: String): Int {
    var value = 0
    try {
        value = text.toInt()
    } catch (nfe: NumberFormatException) {
        value = 0
    }
    return value
}

fun getFloatValue(view: TextView): Float {
    var value = 0f
    try {
        value = view.text.toString().toFloat()
    } catch (nfe: NumberFormatException) {
        value = 1400f
    }
    return value
}