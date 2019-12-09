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
import com.hast.norvialle.utils.getDateInMillis
import com.hast.norvialle.utils.getDateLocal
import com.hast.norvialle.utils.getTimeLocal
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 05.12.2019.
 */
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        try {
            val type = intent?.getStringExtra("TYPE")



            if (type != null) {
                val pm =
                    context!!.getSystemService(Context.POWER_SERVICE) as PowerManager
                val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "NORVIALLE:WAKE")
                wl.acquire(10000)
                when (type){
                    TYPE.OBJECT.name ->{
                        val event = Gson().fromJson(intent?.getStringExtra("EVENT"), Event::class.java)
                        createSampleDataNotification(context, event)
                    }
                    TYPE.ARRAY.name ->{
                        val events = Gson().fromJson(intent?.getStringExtra("EVENT"), ArrayList<Event>()::class.java)
                        createSampleDataNotification(context, events)
                    }
                }


                wl.release()
            }
        } catch (e:Exception){
            e.printStackTrace()
        }
    }



    fun setAlarm(context: Context, event: Event, settings: Settings) {
        val time: Long = event.time - settings.timeBeforeShoot

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, AlarmReceiver::class.java)
        i.putExtra("EVENT", Gson().toJson(event))
        i.putExtra("TYPE", TYPE.OBJECT.name)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)

        am.setExact(AlarmManager.RTC_WAKEUP, time, pi) // time is exact - not relative
        Log.i("AlarmReceiver", "ALARM is set to: "+ getDateLocal(time) +" at: " +getTimeLocal(time))
    }

    fun setAlarms(context: Context, events: ArrayList<Event> , settings: Settings, dayBefore:Boolean) {
        for (event in events) {
            if (dayBefore && getDateInMillis(event.time) > getDateInMillis(System.currentTimeMillis())){
                Log.d("ALA", "FOUND")
            }
        }

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, AlarmReceiver::class.java)
        i.putExtra("EVENT", Gson().toJson(events))
        i.putExtra("TYPE", TYPE.ARRAY.name)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)
        am.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi)
    }



    fun cancelAlarm(context: Context) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val sender = PendingIntent.getBroadcast(context, 0, intent, 0)
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
    enum class TYPE{
        OBJECT,
        ARRAY
    }
}