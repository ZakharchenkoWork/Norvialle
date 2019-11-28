package com.hast.norvialle.gui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import com.hast.norvialle.Event
import com.hast.norvialle.R
import com.hast.norvialle.gui.utils.AddContactDialog
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
class AddEventActivity : AppCompatActivity() {
    //var  event : Event? = Event()
    val presenter: MainPresenter = MainPresenter
    lateinit var event: Event

    companion object {
        const val EVENT_EXTRA: String = "EVENT"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)

        event = intent.getSerializableExtra(EVENT_EXTRA) as Event

        contact.setText(event.name)
        studio.setOnCheckedChangeListener{ button, isChecked -> studioData.visibility = if(isChecked) View.VISIBLE else View.GONE }
        studio.isChecked = event.orderStudio
        dress.isChecked = event.orderDress
        makeup.isChecked = event.orderMakeup


        val dateParser = SimpleDateFormat("dd,M,yyyy", Locale.getDefault())
        val timeParser = SimpleDateFormat("HH:mm", Locale.getDefault())


        val dateFormatter = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

        var dateToSet = Date(event.time)
        val c = Calendar.getInstance()
        val finalCalendar = Calendar.getInstance()
        finalCalendar.time = dateToSet
        c.time = dateToSet

        val output = dateFormatter.format(dateToSet)
        date.setText(output)
        time.setText(timeFormatter.format(dateToSet))

        date.setOnClickListener {
            c.time = dateToSet

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    dateToSet =
                        dateParser.parse("" + dayOfMonth + "," + (monthOfYear + 1) + "," + year)
                    date.setText(dateFormatter.format(dateToSet))
                    finalCalendar.set(year, monthOfYear, dayOfMonth)

                },
                year,
                month,
                day
            )

            dpd.show()
        }

        time.setOnClickListener {
            c.time = dateToSet
            val hour = c.get(Calendar.HOUR)
            val minute = c.get(Calendar.MINUTE)

            val tpd =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->

                    Toast.makeText(this, h.toString() + " : " + m, Toast.LENGTH_LONG).show()

                    var timeToSet = timeParser.parse("" + h.toString() + ":" + m)
                    time.setText(timeFormatter.format(timeToSet))
                    finalCalendar.set(Calendar.MINUTE, m);
                    finalCalendar.set(Calendar.HOUR_OF_DAY, h);
                }), hour, minute, true)

            tpd.show()
        }

        contact.setMovementMethod(LinkMovementMethod.getInstance());
        edit.setOnClickListener {
            val dialog =
                AddContactDialog.newInstance(text = "", hint = "Description", isMultiline = true)
            dialog.nameText = event.name
            dialog.linkText = event.link
            dialog.onOk = { name, link ->
                event.name = name
                event.link = link
                contact.setText(name)
                // do something
            }
            dialog.show(supportFragmentManager, "editDescription")
        }

        ok.setOnClickListener {
            event.time = finalCalendar.timeInMillis
            event.description = description.text.toString()
            event.orderStudio = studio.isChecked
            event.orderDress = dress.isChecked
            event.orderMakeup = makeup.isChecked
            presenter.addEvent(event)

            finish()
        }
    }
}