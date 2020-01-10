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

fun getFloatValue(view: TextView, default: Float = 0f): Float {
    var value = default
    try {
        value = view.text.toString().toFloat()
    } catch (nfe: NumberFormatException) {
    }
    return value
}

fun extractFromFloat(float: Float): String {
    return ("" + float).replace(".0", "")
}
fun extractFromFloat(float: String): String {
    return float.replace(".0", "")
}
