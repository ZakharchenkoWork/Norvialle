package com.hast.norvialle.gui.calendar

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.applandeo.materialcalendarview.EventDay
import com.hast.norvialle.R

import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.main.MainActivity
import com.hast.norvialle.gui.main.MainActivity.Companion.SCROLL_TO
import com.hast.norvialle.utils.getDateInMillis
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class CalendarActivity : AppCompatActivity() {
    val presenter: MainPresenter =
        MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        setSupportActionBar(toolbar)
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.calendar);
        }

        val calendarEvents: MutableList<EventDay> = ArrayList()
        val events = MainPresenter.events
        var eventsThisDay = 0
        var date = 0L
        for (event in events) {
            val eventDate = getDateInMillis(event.time)
            if (date != eventDate){
                eventsThisDay++
                date = eventDate
            }
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = Date(event.time)
            calendarEvents.add(EventDay(calendar,
            when(eventsThisDay){
                1 -> { R.drawable.photoshoot}
                2 -> {R.drawable.photoshoot2}
                3 -> {R.drawable.photoshoot3}
                4 -> {R.drawable.photoshoot4}
                else ->{
                    R.drawable.photoshoot5 }
            }
            ))
        }

        calendarView.setEvents(calendarEvents)
        calendarView.setOnDayClickListener{
            if (it!=null){
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(SCROLL_TO, getDateInMillis(it.calendar.time.time))
                startActivity(intent)

            }
        }


    }

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

}