package com.hast.norvialle.utils.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hast.norvialle.R
import com.hast.norvialle.data.Event
import com.hast.norvialle.gui.main.MainActivity
import com.hast.norvialle.utils.getTime
import com.hast.norvialle.utils.getTimeLocal
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 05.12.2019.
 */


    fun createNotificationChannels(context: Context) {
        // 1
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            var channelId = "${context.packageName}-${context.getString(R.string.app_name)}"
            var channel = NotificationChannel(channelId+"-SINGLE", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH)
            channel.description = "App notification channel."
            channel.setShowBadge(false)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            channel = NotificationChannel(channelId+"-MULTIPLE", context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }
    }

fun composeBigText(context: Context, event: Event): String {
    var text = ""
    if (!event.studioName.equals("")){
        text += context.getString(R.string.shoot_location, event.studioName)
    }
    if (!event.studioAddress.equals("")){
        text += context.getString(R.string.shoot_address, event.studioAddress)
    }
    if (!event.name.equals("")){
        text += context.getString(R.string.shoot_client, event.name)
    }
    return text
    //return "Локация: " + event.studioName + " по адрессу: " + event.studioAddress + " Клиент: " + event.name)

}
fun composeBigText(context: Context, events: ArrayList<Event>): String {

    var text = ""
    for (event in events) {
        if(event.time > 0){
           text += getTimeLocal(event.time) +" - "
        }
        if (!event.studioName.equals("")){
            text += context.getString(R.string.shoot_location, event.studioName)
        }
        if (!events.last().equals(event)) {
            text += "\n"
        }
    }

    return text
    //return "Локация: " + event.studioName + " по адрессу: " + event.studioAddress + " Клиент: " + event.name)

}
fun createSampleDataNotification(context: Context, event: Event){

    val channelId = "${context.packageName}-${context.getString(R.string.app_name)}-SINGLE"

    val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
        setSmallIcon(R.drawable.icon) // 3
        setContentTitle(context.getString(R.string.shoot_in_time, getTimeLocal(event.time))) // 4
        setContentText(context.getString(R.string.shoot_location, event.studioName)) // 5
        setStyle(NotificationCompat.BigTextStyle().bigText(composeBigText(context, event))) // 6
        priority = NotificationCompat.PRIORITY_DEFAULT // 7
        setAutoCancel(true) // 8

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        setContentIntent(pendingIntent)
    }

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(1001, notificationBuilder.build())
}
fun createSampleDataNotification(context: Context, events: java.util.ArrayList<Event>, isTommorrow: Boolean) {
    if (events.isEmpty()){
        return
    }

    val channelId = "${context.packageName}-${context.getString(R.string.app_name)}-MULTIPLE"

    val notificationBuilder = NotificationCompat.Builder(context, channelId).apply {
        setSmallIcon(R.drawable.icon) // 3
        setContentTitle(context.getString(if (isTommorrow) R.string.schedule_for_tomorrow else R.string.schedule_for_today )) // 4
        setContentText(context.getString(R.string.planned_x_events_open_to_view, context.resources.getQuantityString(R.plurals.plurals_shoot, events.size, events.size))) // 5
        setStyle(NotificationCompat.BigTextStyle().bigText(composeBigText(context, events))) // 6
        priority = NotificationCompat.PRIORITY_DEFAULT // 7
        setAutoCancel(true) // 8

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        setContentIntent(pendingIntent)
    }

    val notificationManager = NotificationManagerCompat.from(context)
    notificationManager.notify(1001, notificationBuilder.build())
}
