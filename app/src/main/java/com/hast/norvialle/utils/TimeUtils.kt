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

fun showTimePickerDialog(context: Context, textView : TextView){
    var time = textView.text.toString().split(":")
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
    val calendar = Calendar.getInstance()
    calendar.timeZone= TimeZone.getTimeZone("UTC")
    val tpd =
        android.app.TimePickerDialog(
            context,
            android.app.TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                calendar.set(Calendar.MINUTE, m);
                calendar.set(Calendar.HOUR_OF_DAY, h);
                textView.setText(timeFormatter.format(Date(calendar.timeInMillis)))
            }),
            time[0].toInt(),
            time[1].toInt(),
            true
        )

    tpd.show()
}
fun getTime(textView : TextView): Long {
    val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    timeFormatter.timeZone = TimeZone.getTimeZone("UTC")
    return timeFormatter.parse(textView.text.toString()).time
}