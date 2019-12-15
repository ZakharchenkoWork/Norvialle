package com.hast.norvialle.utils

import android.content.Context
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Konstantyn Zakharchenko on 27.11.2019.
 */

fun getDate(date: Long): String {
    val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    dateFormatter.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormatter.format(date)
}
fun getDateLocal(date: Long): String {
    val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return dateFormatter.format(date)
}
 fun getDateInMillis(time: Long): Long {

        val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return dateFormatter.parse(dateFormatter.format(time)).time

}

    fun getTime(date: Long): String {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
    return timeFormatter.format(date)
}
fun getTimeLocal(date: Long): String {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormatter.format(date)
}

fun showTimePickerDialog(context: Context, textView : TextView, defaultTime : Long = 0){

        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.getTimeZone("UTC")

    try {
        calendar.time = timeFormatter.parse(textView.text.toString())
    } catch (e : Exception){
        if (defaultTime !=  0L){
            calendar.time = Date(defaultTime)
        } else{
            calendar.time = Date(System.currentTimeMillis())
        }
    }

    val tpd =
        android.app.TimePickerDialog(
            context,
            android.app.TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                calendar.set(Calendar.MINUTE, m);
                calendar.set(Calendar.HOUR_OF_DAY, h);
                textView.setText(timeFormatter.format(Date(calendar.timeInMillis)))
            }),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

    tpd.show()
}
fun getTime(textView : TextView): Long {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
    return timeFormatter.parse(textView.text.toString()).time
}

fun getTimeLocal(textView : TextView): Long {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormatter.parse(textView.text.toString()).time
}
fun getDateLocal(textView : TextView): Long {
    val timeFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    return timeFormatter.parse(textView.text.toString()).time
}
fun getDate(textView : TextView): Long {
    val timeFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
    return timeFormatter.parse(textView.text.toString()).time
}