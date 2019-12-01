package com.hast.norvialle.gui.main

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.Event
import com.hast.norvialle.data.PhotoRoom
import com.hast.norvialle.data.Studio
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.dialogs.OneButtonDialog
import com.hast.norvialle.gui.dialogs.PricePickerDialog
import com.hast.norvialle.gui.studio.StudiosListActivity
import com.hast.norvialle.gui.utils.AddContactDialog
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
class AddEventActivity : AppCompatActivity() {
    //var  event : Event? = Event()
    val presenter: MainPresenter =
        MainPresenter
    lateinit var event: Event
    lateinit var finalCalendar: Calendar
    lateinit var finalMakupCalendar: Calendar

    companion object {
        const val EVENT_EXTRA: String = "EVENT"
        const val PICK_STUDIO: Int = 191
        const val PICK_CONTACT: Int = 232
        const val ONE_HOUR_MILLIS: Long = 60*60*1000
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        setSupportActionBar(toolbar)
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.adding_event);
        }

        event = intent.getSerializableExtra(EVENT_EXTRA) as Event


        contact.setText(if (!event.name.equals("")) event.name else getString(R.string.name))
        studio.setOnCheckedChangeListener { button, isChecked ->
            studioData.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        makeup.setOnCheckedChangeListener { buttonView, isChecked ->
            makeupLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        description.setText(event.description)
        studio.isChecked = event.orderStudio
        dress.isChecked = event.orderDress
        makeup.isChecked = event.orderMakeup
        phone.setText(event.contactPhone)

        totalPrice.setText("" + event.totalPrice)
        paid.setText("" + event.paidPrice)
        studioName.setText(event.studioName)
        studioAddress.setText(event.studioAddress)
        makeupPrice.setText(""+event.makeupPrice)
        studioRoom.setText(event.studioRoom)

        val dateParser = SimpleDateFormat("dd,M,yyyy", Locale.getDefault())
        val timeParser = SimpleDateFormat("HH:mm", Locale.getDefault())


        val dateFormatter = SimpleDateFormat("dd:MM:yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

        var dateToSet = Date(event.time)
        val c = Calendar.getInstance()
        finalMakupCalendar = Calendar.getInstance()
        finalCalendar = Calendar.getInstance()
        finalCalendar.time = dateToSet
        c.time = dateToSet
        if(event.makeupTime != 0L) {
            finalMakupCalendar.time = Date(event.makeupTime)
            makeupTime.setText(timeFormatter.format(Date(event.makeupTime)))
        }


        makeupArtistsName.setText(event.makeupArtistName)
        makeupPrice.setText(""+event.makeupPrice)

        if(event.orderMakeup){
            makeupLayout.visibility = View.VISIBLE
        } else{
            makeupLayout.visibility = View.GONE
        }

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



                    var timeToSet = timeParser.parse("" + h.toString() + ":" + m)
                    time.setText(timeFormatter.format(timeToSet))
                    finalCalendar.set(Calendar.MINUTE, m);
                    finalCalendar.set(Calendar.HOUR_OF_DAY, h);
                }), hour, minute, true)

            tpd.show()
        }
        makeupTime.setOnClickListener {
            if (event.makeupTime == 0L){
                finalMakupCalendar.time = Date(dateToSet.time - ONE_HOUR_MILLIS)
            } else{
                finalMakupCalendar.time = Date(event.makeupTime)
            }


            val hour = finalMakupCalendar.get(Calendar.HOUR)
            val minute =finalMakupCalendar.get(Calendar.MINUTE)

            val tpd =
                TimePickerDialog(this, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                    var timeToSet = timeParser.parse("" + h.toString() + ":" + m)
                    makeupTime.setText(timeFormatter.format(timeToSet))
                    finalMakupCalendar.set(Calendar.MINUTE, m);
                    finalMakupCalendar.set(Calendar.HOUR_OF_DAY, h);
                }), hour, minute, true)

            tpd.show()
        }

        contact.setOnClickListener {
            showContactDialog()
        }
        contactsList.setOnClickListener {
            openContactsList()
        }


        studiosList.setOnClickListener { openStudiosList() }
        totalPrice.setOnClickListener {
            OneButtonDialog(this, OneButtonDialog.DIALOG_TYPE.INPUT_ONLY)
                .setTitle(getString(R.string.totalPrice))
                .setMessage(""+getFloatValue(totalPrice))
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setOkListener { totalPrice.setText(("" + it).replace(".0", "")) }
                .build()

        }
        paid.setOnClickListener {
            OneButtonDialog(this, OneButtonDialog.DIALOG_TYPE.INPUT_ONLY)
                .setTitle(getString(R.string.paid))
                .setMessage(""+getFloatValue(paid))
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setOkListener { paid.setText(("" + it).replace(".0", "")) }
                .build()
        }
        makeupPrice.setOnClickListener {
            OneButtonDialog(this, OneButtonDialog.DIALOG_TYPE.INPUT_ONLY)
                .setTitle(getString(R.string.makeupPrice))
                .setMessage(""+getFloatValue(makeupPrice))
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setOkListener { makeupPrice.setText(("" + it).replace(".0", "")) }
                .build()
        }

    }

    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.add_studio_menu, menu);
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
            }
            R.id.save -> {
                event.time = finalCalendar.timeInMillis
                event.contactPhone = phone.text.toString()
                event.description = description.text.toString()
                event.orderStudio = studio.isChecked
                if (studio.isChecked) {
                    event.studioName = studioName.text.toString()
                    event.studioRoom = studioRoom.text.toString()
                    event.studioAddress = studioAddress.text.toString()
                    event.studioGeo = studioGeo.text.toString()
                    event.studioPhone = studioPhone.text.toString()
                }
                if (makeup.isChecked) {
                    event.makeupArtistName = makeupArtistsName.text.toString()
                    event.makeupPrice = getIntValue(makeupPrice)
                    event.makeupTime = finalMakupCalendar.timeInMillis
                }

                event.orderDress = dress.isChecked
                event.orderMakeup = makeup.isChecked

                event.totalPrice = getIntValue(totalPrice)
                event.paidPrice = getIntValue(paid)

                MainPresenter.addEvent(event)
                finish()
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private fun getFloatValue(view: TextView): Float {
        var value = 0f
        try {
            value = view.text.toString().toFloat()
        } catch (nfe: NumberFormatException) {
            value = 1400f
        }
        return value
    }

    private fun getIntValue(view: TextView): Int {
        var value = 0
        try {
            value = view.text.toString().toInt()
        } catch (nfe: NumberFormatException) {
            value = 0
        }
        return value
    }

    private fun openStudiosList() {
        var intent = Intent(this, StudiosListActivity::class.java)
        intent.putExtra(StudiosListActivity.IS_FOR_RESULT, true)
        startActivityForResult(intent, PICK_STUDIO)
    }

    private fun openContactsList() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        startActivityForResult(intent, PICK_CONTACT)
    }

    fun showContactDialog() {
        val dialog =
            AddContactDialog.newInstance()
        dialog.nameText = event.name
        dialog.linkText = event.link
        dialog.phoneText = event.contactPhone
        dialog.onOk = { contactData ->
            event.name = contactData.name
            event.link = contactData.link
            event.contactPhone = contactData.phone
            contact.setText(contactData.name)
            phone.setText(contactData.phone)

        }
        dialog.show(supportFragmentManager, "editDescription")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_STUDIO) {
                var studio = data?.getSerializableExtra("STUDIO") as Studio
                var photoRoom = data?.getSerializableExtra("ROOM") as PhotoRoom
                studioName.setText(studio.name)
                studioAddress.setText(studio.address)
                studioPhone.setText(studio.phone)
                studioGeo.setText(studio.link)
                studioRoom.setText(photoRoom.name)
            } else if (requestCode == PICK_CONTACT) {
                val contactUri = data?.getData();
                /*   val i = {1}
                    val projection : ArrayList<String> =  {ContactsContract.CommonDataKinds.Phone.NUMBER};*/
                /*Cursor cursor = getContext().getContentResolver().query(contactUri, projection,
                    null, null, null);

                // If the cursor returned is valid, get the phone number
                if (cursor != null && cursor.moveToFirst()) {
                    int numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = cursor.getString(numberIndex);
                    // Do something with the phone number

                }*/
            }
        }
    }



}