package com.hast.norvialle.presenataion.base.events

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.hast.norvialle.R
import com.hast.norvialle.data.Dress
import com.hast.norvialle.data.Event
import com.hast.norvialle.presenataion.base.BaseActivity
import com.hast.norvialle.domain.MainPresenter
import com.hast.norvialle.presenataion.base.contacts.AddContactActivity
import com.hast.norvialle.presenataion.base.contacts.ContactsListFragment
import com.hast.norvialle.presenataion.utils.dialogs.PickDateDialog
import com.hast.norvialle.presenataion.utils.dialogs.SimpleDialog
import com.hast.norvialle.presenataion.base.dresses.DressesListFragment
import com.hast.norvialle.presenataion.base.makeup.AssistantListFragment
import com.hast.norvialle.presenataion.base.makeup.MakeupListFragment
import com.hast.norvialle.presenataion.base.studio.StudiosListFragment
import com.hast.norvialle.presenataion.utils.dialogs.AddContactDialog
import com.hast.norvialle.presenataion.utils.FullScreenPictureActivity
import com.hast.norvialle.presenataion.utils.dialogs.priceInputDialog
import com.hast.norvialle.utils.*
import com.hast.norvialle.utils.notifications.setAlarmForEvent
import kotlinx.android.synthetic.main.activity_add_event.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Konstantyn Zakharchenko on 20.09.2019.
 */
class AddEventActivity : BaseActivity() {

    lateinit var event: Event
    val addContactDialog = AddContactDialog.newInstance()

    companion object {
        const val EVENT_EXTRA: String = "EVENT"
        const val ONE_HOUR_MILLIS: Long = 60 * 60 * 1000
    }

    fun setAdapter(dresses: ArrayList<Dress>) {
        val dressesPicturesAdapter =
            DressesPicturesAdapter(
                dresses,
                this
            )
        dressesList.adapter = dressesPicturesAdapter
        dressesPicturesAdapter.onViewDressListener =
            DressesPicturesAdapter.OnViewDressListener { it ->
                openPictureFullScreen(
                    it
                )
            }
        dressesListPick.setOnClickListener { openDressesList(dresses) }
    }

    fun openPictureFullScreen(dress: Dress) {

        val intent = Intent(this, FullScreenPictureActivity::class.java)
        intent.putExtra(FullScreenPictureActivity.DATA_TYPE, dress)
        startActivity(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        setSupportActionBar(toolbar)

        event = intent.getSerializableExtra(EVENT_EXTRA) as Event
        dressesList.layoutManager = GridLayoutManager(this, 5)

        contact.setText(if (!event.name.equals("")) event.name else getString(R.string.name))
        studio.setOnCheckedChangeListener { button, isChecked ->
            studioData.visibility = if (isChecked) View.VISIBLE else View.GONE
            location.visibility = if (!isChecked) View.VISIBLE else View.GONE
        }
        makeup.setOnCheckedChangeListener { buttonView, isChecked ->
            makeupLayout.visibility = if (isChecked) View.VISIBLE else View.GONE
        }
        setAdapter(event.dresses)
        dress.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                dressesList.visibility = View.VISIBLE
                dressesListPick.visibility = View.VISIBLE

                val adapter = dressesList.adapter
                if (adapter != null && adapter is DressesPicturesAdapter) {
                    val dresses = adapter.items
                    if (dresses.isEmpty()) {
                        openDressesList(dresses)
                    } else {
                        setAdapter(dresses)
                    }
                }
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
        if (!event.orderStudio){
            location.setText(event.studioName)
        }
        studioName.setText(event.studioName)
        studioAddress.setText(event.studioAddress)

        studioRoom.setText(event.studioRoom)

        val dateFormatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault())

        var dateToSet = Date(event.time)
        val c = Calendar.getInstance()


        c.time = dateToSet
        if (event.makeupTime != 0L) {

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
            PickDateDialog(this).build(dateFormatter.parse(date.text.toString()).time) {
                date.setText(dateFormatter.format(it))
            }
        }

        time.setOnClickListener {
            showTimePickerDialog(this, time)
        }


        makeupTime.setOnClickListener {
            var makeupTimeValue = 0L

            if (event.makeupTime == 0L) {
                makeupTimeValue = getTime(time) - ONE_HOUR_MILLIS
            } else {
                makeupTimeValue = event.makeupTime
            }

            showTimePickerDialog(this, makeupTime, makeupTimeValue)

        }

        contact.setOnClickListener {
            showContactDialog()
        }
        contactsList.setOnClickListener {
            openContactsList()
        }


        studiosList.setOnClickListener { openStudiosList() }
        totalPrice.setOnClickListener {
            priceInputDialog(
                this,
                R.string.totalPrice,
                getFloatValue(totalPrice, 1400f)
            ) {
                totalPrice.text = stringPriceWithPlaceholder(it, "0")
            }
        }
        paid.setOnClickListener {
            priceInputDialog(
                this,
                R.string.paid,
                getFloatValue(paid, 1400f)
            ) {
                paid.text = stringPriceWithPlaceholder(it, "0")
            }
        }
        makeupPrice.setOnClickListener {
            priceInputDialog(
                this,
                R.string.makeupPrice,
                getFloatValue(makeupPrice)
            ) {
                makeupPrice.text = stringPriceWithPlaceholder(it, R.id.price)
            }
        }

        makeupArtistsList.setOnClickListener { openMakeupArtistsList() }

        assistant.isChecked = event.orderAssistant
        if (event.orderAssistant) {
            assistantLayout.visibility = View.VISIBLE
        } else {
            assistantLayout.visibility = View.GONE
        }

        assistant.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                assistantLayout.visibility = View.VISIBLE
            } else {
                assistantLayout.visibility = View.GONE
            }
        }
        assistantPrice.setText("" + event.assistantPrice)
        assistantName.setText(event.assistantName)
        assistantPhone.setText(event.assistantPhone)
        assistantsList.setOnClickListener { openAssistantsList() }


        hideKeyboard()
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.save -> {
                try {
                    event.time = getDateLocal(date) + getTime(time)
                    event.contactPhone = phone.text.toString()
                    event.description = description.getText()
                    event.orderStudio = studio.isChecked
                    if (studio.isChecked) {

                            event.studioName = checkToSave(studioName)
                            event.studioRoom = checkToSave(studioRoom)
                            event.studioAddress = checkToSave(studioAddress)

                        event.studioGeo = studioGeo.text.toString()
                        event.studioPhone = studioPhone.getText()
                    } else {
                        event.studioName = checkToSave(location)
                    }

                    event.orderMakeup = makeup.isChecked
                    if (makeup.isChecked) {
                        event.makeupArtistName = checkToSave(makeupArtistsName)
                        event.makeupPrice = getIntValue(makeupPrice)
                        try {
                            event.makeupTime = getTimeLocal(makeupTime)
                        } catch (e: Exception) {
                            event.makeupTime = getTimeLocal(time) - ONE_HOUR_MILLIS
                        }
                        event.makeupPhone = makeupArtistsPhone.getText()
                    }
                    event.orderAssistant = assistant.isChecked
                    if (assistant.isChecked) {
                        event.assistantName = checkToSave(assistantName)
                        event.assistantPrice = getIntValue(assistantPrice)
                        event.assistantPhone = assistantPhone.getText()
                    }

                   event.orderDress = dress.isChecked
                    if (dress.isChecked) {
                        val adapter = dressesList.adapter
                        event.dresses = if (adapter is DressesPicturesAdapter) adapter.items else ArrayList()
                    }
                    event.totalPrice = getIntValue(totalPrice)
                    event.paidPrice = getIntValue(paid)

                    MainPresenter.addEvent(event)

                    setAlarmForEvent(this, event = event, settings = MainPresenter.settings)
                    finish()
                }catch (dataCheckFailed : InvalidSavingData){
                    fastToast(dataCheckFailed)
                    return true
                }
            }

        }
        return super.onOptionsItemSelected(item);
    }

    override fun getMenuRes(): Int {
        return R.menu.menu_save
    }

    override fun getMenuTitleRes(): Int {
        return R.string.adding_event
    }

    private fun openStudiosList() {
        showFragment(StudiosListFragment.newInstance { studio, photoRoom ->
            studioName.setText(studio.name)
            studioAddress.setText(studio.address)
            studioPhone.setText(studio.phone)
            studioGeo.setText(studio.link)
            studioRoom.setText(photoRoom.name)
        })

    }

    private fun openDressesList(dresses: ArrayList<Dress>) {
        showFragment(DressesListFragment.newInstance(dresses) {
            setAdapter(it)
            dressesList.visibility = View.VISIBLE
        })
    }

    private fun openMakeupArtistsList() {
        showFragment(MakeupListFragment.newInstance { makeupArtist ->
            makeupPrice.setText("" + makeupArtist.defaultPrice)
            makeupArtistsName.setText(makeupArtist.name)
            makeupArtistsPhone.setText(makeupArtist.phone)
        })
    }
   private fun openAssistantsList() {
        showFragment(AssistantListFragment.newInstance { assistant ->
            assistantPrice.setText("" + assistant.defaultPrice)
            assistantName.setText(assistant.name)
            assistantPhone.setText(assistant.phone)
        })
    }

    private fun openContactsList() {
        showFragment(ContactsListFragment.newInstance() {
            event.name = it.name
            event.link = it.link
            event.contactPhone = it.phone
            contact.setText(it.name)
            phone.setText(it.phone)
        })
    }

    fun showContactDialog() {

        addContactDialog.nameText = event.name
        addContactDialog.linkText = event.link
        addContactDialog.phoneText = event.contactPhone
        addContactDialog.onOk = { contactData ->
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
        if (resultCode == Activity.RESULT_OK && requestCode == AddContactActivity.PICK_CONTACT && data != null) {
            data.data?.let { uri ->


                val projection = arrayOf(
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                )
                val cursor: Cursor? = contentResolver
                    .query(uri, projection, null, null, null)
                cursor?.let { it ->
                    it.moveToFirst()

                    val columnPhone: Int =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val columnName: Int =
                        it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                    addContactDialog.nameText = it.getString(columnName)
                    addContactDialog.phoneText = it.getString(columnPhone)
                    addContactDialog.update()
                    cursor.close()
                }
            }
        }
    }


}