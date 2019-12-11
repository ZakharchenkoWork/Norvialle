package com.hast.norvialle.gui.calendar

import android.view.View
import com.applandeo.materialcalendarview.EventDay
import com.hast.norvialle.R
import com.hast.norvialle.gui.BaseFragment
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.utils.getDateInMillis
import kotlinx.android.synthetic.main.activity_calendar.view.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class CalendarFragment : BaseFragment() {

    companion object {
        fun newInstance(): BaseFragment {
            return CalendarFragment()
        }
    }

    override fun getContentId(): Int {
        return R.layout.activity_calendar
    }

    override fun getMenuId(): Int {
        return R.menu.menu_empty
    }

    override fun getMenuTitleId(): Int {
        return R.string.calendar
    }

    override fun onCreateView(root: View): View {
        val calendarEvents: MutableList<EventDay> = ArrayList()
        val events = MainPresenter.events
        var eventsThisDay = 0
        var date = 0L
        for (event in events) {
            val eventDate = getDateInMillis(event.time)
            if (date != eventDate) {
                eventsThisDay++
                date = eventDate
            }
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = Date(event.time)
            calendarEvents.add(
                EventDay(
                    calendar,
                    when (eventsThisDay) {
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
                )
            )
        }

        root.calendarView.setEvents(calendarEvents)
        root.calendarView.setOnDayClickListener {
            if (it != null) {
                openEventsList(getDateInMillis(it.calendar.time.time))
            }
        }
        return root
    }


/*
    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_back, menu);
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item);
    }
*/

}