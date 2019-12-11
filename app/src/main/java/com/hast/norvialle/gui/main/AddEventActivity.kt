package com.hast.norvialle.gui.main

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.*
import com.hast.norvialle.gui.BaseActivity
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.contacts.AddContactActivity
import com.hast.norvialle.gui.contacts.ContactsListActivity
import com.hast.norvialle.gui.contacts.ContactsListActivity.Companion.RETURN_DATA
import com.hast.norvialle.gui.dialogs.SimpleDialog
import com.hast.norvialle.gui.dresses.DressesListActivity
import com.hast.norvialle.gui.makeup.MakeupListActivity
import com.hast.norvialle.gui.studio.StudiosListActivity
import com.hast.norvialle.gui.utils.AddContactDialog
import com.hast.norvialle.gui.utils.FullScreenPictureActivity
import com.hast.norvialle.utils.getFloatValue
import com.hast.norvialle.utils.getIntValue
import com.hast.norvialle.utils.notifications.AlarmReceiver
import com.hast.norvialle.utils.notifications.setAlarmForEvent
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_add_event.contactsList
import kotlinx.android.synthetic.main.activity_add_event.phone
import kotlinx.android.synthetic.main.activity_add_event.toolbar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
class AddEventActivity : BaseActivity() {

    lateinit var event: Event
    lateinit var finalCalendar: Calendar
    lateinit var finalMakupCalendar: Calendar
    val addContactDialog = AddContactDialog.newInstance()
    companion object {
        const val EVENT_EXTRA: String = "EVENT"
        const val PICK_STUDIO: Int = 191
        const val PICK_CONTACT: Int = 232
        const val PICK_DRESSES: Int = 540
        const val PICK_MAKEUP_ARTIST: Int = 168
        const val ONE_HOUR_MILLIS: Long = 60 * 60 * 1000
    }

    fun setAdapter() {
        val dressesPicturesAdapter = DressesPicturesAdapter(event.dresses, this)
        dressesList.adapter = dressesPicturesAdapter
        dressesPicturesAdapter.onViewDressListener =
            DressesPicturesAdapter.OnViewDressListener { it -> openPictureFullScreen(it) }

    }

    fun openPictureFullScreen(dress: Dress) {

        val intent = Intent(this, FullScreenPictureActivity::class.java)
        intent.putExtra(FullScreenPictureActivity.PICTURE_FILE_NAME, dress.fileName)
        intent.putExtra(FullScreenPictureActivity.COMMENT, dress.comment)
        startActivity(intent)

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
        dressesList.layoutManager = GridLayoutManager(this, 5)

        contact.setText(if (!event.name.equals("")) event.name else getString(R.string.name))
        studio.setOnCheckedChangeListener { button, isChecked ->
            studioData.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        makeup.setOnCheckedChangeListener { buttonView, isChecked ->
            makeupLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        dress.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dressesList.visibility = View.VISIBLE
                dressesListPick.visibility = View.VISIBLE
                if (event.dresses.isEmpty()) {
                    openDressesList(event.dresses)
                } else {
                    setAdapter()
                }
                dressesListPick.setOnClickListener { openDressesList(event.dresses) }
            } else {
                dressesListPick.visibility = View.GONE
                dressesList.visibility = View.GONE
            }

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
        if (event.makeupTime != 0L) {
            finalMakupCalendar.time = Date(event.makeupTime)
            makeupTime.setText(timeFormatter.format(Date(event.makeupTime)))
        }


        makeupArtistsName.setText(event.makeupArtistName)
        makeupPrice.setText("" + event.makeupPrice)
        makeupArtistsPhone.setText(event.makeupPhone)

        if (event.orderMakeup) {
            makeupLayout.visibility = View.VISIBLE
        } else {
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
            val hour = c.get(Calendar.HOUR_OF_DAY)
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
            if (event.makeupTime == 0L) {
                finalMakupCalendar.time = Date(dateToSet.time - ONE_HOUR_MILLIS)
            } else {
                finalMakupCalendar.time = Date(event.makeupTime)
            }


            val hour = finalMakupCalendar.get(Calendar.HOUR_OF_DAY)
            val minute = finalMakupCalendar.get(Calendar.MINUTE)

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
            SimpleDialog(this, SimpleDialog.DIALOG_TYPE.INPUT_ONLY)
                .setTitle(getString(R.string.totalPrice))
                .setMessage("" + getFloatValue(totalPrice))
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setOkListener { totalPrice.setText(("" + it).replace(".0", "")) }
                .build()

        }
        paid.setOnClickListener {
            SimpleDialog(this, SimpleDialog.DIALOG_TYPE.INPUT_ONLY)
                .setTitle(getString(R.string.paid))
                .setMessage("" + getFloatValue(paid))
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setOkListener { paid.setText(("" + it).replace(".0", "")) }
                .build()
        }
        makeupPrice.setOnClickListener {
            SimpleDialog(this, SimpleDialog.DIALOG_TYPE.INPUT_ONLY)
                .setTitle(getString(R.string.makeupPrice))
                .setMessage("" + getFloatValue(makeupPrice))
                .setInputType(InputType.TYPE_CLASS_NUMBER)
                .setOkListener { makeupPrice.setText(("" + it).replace(".0", "")) }
                .build()
        }

        makeupArtistsList.setOnClickListener { openMakeupArtistsList() }
        hideKeyboard()
    }

    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
        getMenuInflater().inflate(R.menu.menu_save, menu);
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
                    if (makeupTime.text.toString().equals(getString(R.string.one_hour_before))) {
                        event.makeupTime = finalMakupCalendar.timeInMillis
                    } else {
                        event.makeupTime = finalCalendar.timeInMillis - ONE_HOUR_MILLIS
                    }
                    event.makeupPhone = makeupArtistsPhone.text.toString()
                }

                event.orderDress = dress.isChecked
                event.orderMakeup = makeup.isChecked

                event.totalPrice = getIntValue(totalPrice)
                event.paidPrice = getIntValue(paid)

                MainPresenter.addEvent(event)

                setAlarmForEvent(this, event = event, settings = MainPresenter.settings)
                finish()
            }

        }
        return super.onOptionsItemSelected(item);
    }


    private fun openStudiosList() {
        var intent = Intent(this, StudiosListActivity::class.java)
        intent.putExtra(StudiosListActivity.IS_FOR_RESULT, true)
        startActivityForResult(intent, PICK_STUDIO)
    }

    private fun openDressesList(dresses: ArrayList<Dress>) {
        var intent = Intent(this, DressesListActivity::class.java)
        intent.putExtra(DressesListActivity.IS_FOR_RESULT, true)
        intent.putExtra(DressesListActivity.PICKED_DRESSES, dresses)
        startActivityForResult(intent, PICK_DRESSES)
    }

    private fun openMakeupArtistsList() {
        var intent = Intent(this, MakeupListActivity::class.java)
        intent.putExtra(MakeupListActivity.IS_FOR_RESULT, true)
        startActivityForResult(intent, PICK_MAKEUP_ARTIST)
    }

    private fun openContactsList() {
        var intent = Intent(this, ContactsListActivity::class.java)
        intent.putExtra(ContactsListActivity.IS_FOR_RESULT, true)
        startActivityForResult(intent, PICK_CONTACT)
    }

    fun showContactDialog() {

        addContactDialog .nameText = event.name
        addContactDialog .linkText = event.link
        addContactDialog .phoneText = event.contactPhone
        addContactDialog .onOk = { contactData ->
            event.name = contactData.name
            event.link = contactData.link
            event.contactPhone = contactData.phone
            contact.setText(contactData.name)
            phone.setText(contactData.phone)
            SimpleDialog(this, SimpleDialog.DIALOG_TYPE.MESSAGE_ONLY)
                .setTitle(getString(R.string.adding_contact))
                .setMessage(getString(R.string.dialog_add_contact_save))
                .setCancelable(true)
                .setOkListener {
                    MainPresenter.addContact(contactData)
                }.build()
        }
        addContactDialog.show(supportFragmentManager, "editDescription")
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
            } else if (requestCode == PICK_MAKEUP_ARTIST) {
                var makeupArtist = data?.getSerializableExtra("MAKEUP_ARTIST") as MakeupArtist
                makeupPrice.setText("" + makeupArtist.defaultPrice)
                makeupArtistsName.setText(makeupArtist.name)
                makeupArtistsPhone.setText(makeupArtist.phone)

            } else if (requestCode == PICK_DRESSES) {
                event.dresses =
                    data?.getSerializableExtra(DressesListActivity.DRESS) as ArrayList<Dress>
                setAdapter()
                dressesList.visibility = View.VISIBLE
            }  else if (requestCode == AddContactActivity.PICK_CONTACT && data != null) {
                val contactUri = data?.getData();
                val projection = arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                val cursor: Cursor = contentResolver
                    .query(contactUri, projection, null, null, null)
                cursor.moveToFirst()

                val columnPhone: Int = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val columnName: Int = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                addContactDialog .nameText = cursor.getString(columnName)
                addContactDialog .phoneText = cursor.getString(columnPhone)
                addContactDialog.update()
            }else if (requestCode == PICK_CONTACT) {
                var contactData = data?.getSerializableExtra(RETURN_DATA) as Contact
                event.name = contactData.name
                event.link = contactData.link
                event.contactPhone = contactData.phone
                contact.setText(contactData.name)
                phone.setText(contactData.phone)

            }

        }
    }

    fun AppCompatActivity.hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

    }


}