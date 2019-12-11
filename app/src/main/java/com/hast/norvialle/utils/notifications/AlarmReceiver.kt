package com.hast.norvialle.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import com.google.gson.Gson
import com.hast.norvialle.data.Event
import com.hast.norvialle.data.Settings
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.utils.getDateInMillis
import com.hast.norvialle.utils.getDateLocal
import com.hast.norvialle.utils.getTimeLocal
import com.hast.norvialle.utils.notifications.AlarmReceiver.Companion.ALARM_CODE_DAY_BEFORE
import com.hast.norvialle.utils.notifications.AlarmReceiver.Companion.ALARM_CODE_DAY_SAME
import com.hast.norvialle.utils.notifications.AlarmReceiver.Companion.ONE_DAY_MILLIS


/**
 * Created by Konstantyn Zakharchenko on 05.12.2019.
 */
class AlarmReceiver : BroadcastReceiver() {
    companion object {

        val ALARM_CODE_DAY_BEFORE = 666
        val ALARM_CODE_DAY_SAME = 999
        val ONE_DAY_MILLIS : Long = 24 * 60 * 60 * 1000
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i(
            "AlarmReceiver",
            "ALARM is recieved")
        try {
            val type = intent?.getStringExtra("TYPE")
            val day = intent?.getStringExtra("DAY")
            Log.i(
                "AlarmReceiver",
                "ALARM is recieved " + type + " day " + day)


            if (type != null) {
                val pm =
                    context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
                val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NORVIALLE:WAKE")
                wl.acquire(10000)
                when (type) {
                    TYPE.OBJECT.name -> {
                        val event =
                            Gson().fromJson(intent?.getStringExtra("EVENT"), Event::class.java)
                        createSampleDataNotification(context, event)
                    }
                    TYPE.ARRAY.name -> {
                        if (day.equals(DAY.TOMORROW.name)) {
                            createSampleDataNotification(context, getTomorrowEvents(), true)
                        } else if (day.equals(DAY.TODAY.name)) {
                            createSampleDataNotification(context, getTodayEvents(), false)
                        }
                    }
                }


                wl.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
fun setAlarmForEvent(context: Context, event: Event, settings: Settings) {
    cancelAlarm(context, event.additionalId)
    setAlarm(context, event, settings)
}
fun resetDayBeforeAlarm(context: Context, settings: Settings){
    setRepeatingAlarm(context, settings, true)
}
fun resetDaySameAlarm(context: Context, settings: Settings){
    setRepeatingAlarm(context, settings, false)
}
fun resetAlarmsForEvents(context: Context,  eventsList : ArrayList<Event>, settings: Settings){
    val timeNow = System.currentTimeMillis()
    for (event in eventsList) {
        if (event.time > timeNow) {
            cancelAlarm(context, event.additionalId)
            setAlarm(context, event, settings)
        }
    }
}

fun deleteAlarmForEvent(context: Context,  event : Event){
    val timeNow = System.currentTimeMillis()
    if (event.time > timeNow) {
        cancelAlarm(context, event.additionalId)
    }
}
private fun cancelAlarm(context: Context, alarmId: Int) {
    val intent = Intent(context, AlarmReceiver::class.java)
    val sender = PendingIntent.getBroadcast(context, alarmId, intent, 0)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(sender)
}

private fun getTodayEvents(): ArrayList<Event> {

    return getEventsArrayByDay(System.currentTimeMillis(), MainPresenter.getEventsList())
}
private fun getTomorrowEvents(): ArrayList<Event> {

    return getEventsArrayByDay(System.currentTimeMillis()+ AlarmReceiver.ONE_DAY_MILLIS, MainPresenter.getEventsList())
}

private fun getEventsArrayByDay(time : Long,  eventsList : ArrayList<Event>): ArrayList<Event> {
    val thisDayEvents = ArrayList<Event>()
    for (eventFromList in eventsList) {
        if(getDateInMillis(eventFromList.time) == getDateInMillis(time)) {
            thisDayEvents.add(eventFromList)
        }
    }
    return thisDayEvents
}


private fun setRepeatingAlarm(context: Context, settings: Settings, isDayBefore: Boolean){

    var time  = getDateInMillis(System.currentTimeMillis()) + if (isDayBefore)settings.timeOfDayBefore else settings.timeOfSameDay
    if(time < System.currentTimeMillis()){
        time += ONE_DAY_MILLIS
    }
    val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val i = Intent(context, AlarmReceiver::class.java)

    i.putExtra("TYPE", TYPE.ARRAY.name)
    i.putExtra("DAY",  if (isDayBefore) DAY.TOMORROW.name else DAY.TODAY.name)
    val pi = PendingIntent.getBroadcast(context.applicationContext,  if (isDayBefore) ALARM_CODE_DAY_BEFORE else ALARM_CODE_DAY_SAME, i, PendingIntent.FLAG_CANCEL_CURRENT)

    am.setRepeating(AlarmManager.RTC_WAKEUP, time, ONE_DAY_MILLIS, pi) // time is exact - not relative
    Log.i(
        "AlarmReceiver",
        "ALARM is set to: " + getDateLocal(time) + " at: " + getTimeLocal(time)
    )
}

private fun setAlarm(context: Context, event: Event,  settings: Settings) {
    val time: Long = event.time - settings.timeBeforeShoot
    if (time > System.currentTimeMillis()) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, AlarmReceiver::class.java)
        i.putExtra("EVENT", Gson().toJson(event))
        i.putExtra("TYPE", TYPE.OBJECT.name)

        val pi = PendingIntent.getBroadcast(context.applicationContext,event.additionalId , i, PendingIntent.FLAG_CANCEL_CURRENT)

        am.setExact(AlarmManager.RTC_WAKEUP, time, pi) // time is exact - not relative

        Log.i(
            "AlarmReceiver",
            "ALARM is set to: " + getDateLocal(time) + " at: " + getTimeLocal(time)
        )
    } else {
        Log.i("AlarmReceiver", "Too late to send notification")
    }
}
enum class TYPE {
    OBJECT,
    ARRAY
}

enum class DAY {
    TODAY,
    TOMORROW
}
