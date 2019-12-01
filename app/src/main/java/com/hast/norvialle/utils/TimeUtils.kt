package com.hast.norvialle.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Konstantyn Zakharchenko on 27.11.2019.
 */

fun getDate(date: Long): String {
    val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return dateFormatter.format(date)
}
fun getTime(date: Long): String {
    val dateFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormatter.format(date)
}
fun getMillis(date: String): Long {
    val dateFormatter = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
    return dateFormatter.parse(date).time
}

