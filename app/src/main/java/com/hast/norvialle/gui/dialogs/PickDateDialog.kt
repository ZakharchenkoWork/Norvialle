package com.hast.norvialle.gui.dialogs


import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater

import com.applandeo.materialcalendarview.EventDay
import com.hast.norvialle.R
import com.hast.norvialle.data.Event
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.utils.getDateInMillis
import com.hast.norvialle.utils.getTimeLocal
import kotlinx.android.synthetic.main.activity_calendar.view.*
import kotlinx.android.synthetic.main.activity_calendar.view.calendarView
import kotlinx.android.synthetic.main.dialog_calendar.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 15.12.2019.
 */
class PickDateDialog(context: Context) : Dialog(context) {

    var lastDialog: PickDateDialog? = null

    init {
        if (lastDialog != null) {

        }
        lastDialog = this


    }

    fun build(date:Long, onPickListener : (date : Long) -> Unit): PickDateDialog {


        val view = LayoutInflater.from(context).inflate(R.layout.dialog_calendar, null)
        setContentView(view)

        var todayCalendar = Calendar.getInstance()
        todayCalendar.time = Date(date)
        view.calendarView.setDate(todayCalendar)


        val calendarEvents: MutableList<EventDay> = ArrayList()
        val events = MainPresenter.events
        var eventsThisDay = 0
        var date = 0L
        for (event in events) {
            val eventDate = getDateInMillis(event.time)
            if (date == 0L) {
                date = eventDate
            }

            if (date != eventDate) {
                val calendar: Calendar = Calendar.getInstance()
                calendar.time = Date(date)
                calendarEvents.add(
                    EventDay(
                        calendar,
                        getIcon(eventsThisDay)
                    )
                )
                eventsThisDay = 0
                date = eventDate
            }
            eventsThisDay++

        }

        view.calendarView.setEvents(calendarEvents)
        view.calendarView.setOnDayClickListener {
            if (it != null) {
                val pickedDate = getDateInMillis(it.calendar.timeInMillis)
                val eventsOnDay: ArrayList<Event> = ArrayList()
                for (eventThisDay in events) {

                    if (getDateInMillis(eventThisDay.time) == pickedDate) {
                        eventsOnDay.add(eventThisDay)
                    }
                }
                var text = ""
                for (event in eventsOnDay) {
                    text += getTimeLocal(event.time) +  " - " + event.studioName + ", "
                }
                view.daySchedule.text = text
            }
        }

view.ok.setOnClickListener{
    onPickListener.invoke(view.calendarView.selectedDates[0].time.time)
    lastDialog?.dismiss()
    lastDialog = null

}
        super.show()
        return this
    }

    fun getIcon(eventsCount: Int): Int {
        return when (eventsCount) {
            1 -> {
                R.drawable.photoshoot
            }
            2 -> {
                R.drawable.photoshoot2
            }
            3 -> {
                R.drawable.photoshoot3
            }
            4 -> {
                R.drawable.photoshoot4
            }
            else -> {
                R.drawable.photoshoot5
            }
        }
    }
}