package com.hast.norvialle.gui.studio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.PhotoRoom
import com.hast.norvialle.data.Studio
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.gui.dialogs.PricePickerDialog
import com.hast.norvialle.gui.dialogs.SimpleDialog
import com.hast.norvialle.utils.getFloatValue
import kotlinx.android.synthetic.main.activity_add_event.*
import kotlinx.android.synthetic.main.activity_add_studio.*
import kotlinx.android.synthetic.main.activity_add_studio.studioAddress
import kotlinx.android.synthetic.main.activity_add_studio.studioName
import kotlinx.android.synthetic.main.activity_add_studio.studioPhone
import kotlinx.android.synthetic.main.activity_add_studio.toolbar
import kotlinx.android.synthetic.main.item_room.view.*

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AddStudioActivity : AppCompatActivity() {
    companion object {
        val EDIT: Int = 1
        val STUDIO: String = "STUDIO"
    }

    val presenter: MainPresenter = MainPresenter
    var isForResult = true
    lateinit var studio: Studio
    var roomsViews = ArrayList<View>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_studio)
        setSupportActionBar(toolbar)
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.add_studio);
        }
        if (intent?.action != Intent.ACTION_SEND) {
            studio = intent?.extras?.getSerializable(STUDIO) as Studio
        } else {
            //TODO: Add support for Google Maps, Yandex and e.t.c.
            if ("text/plain" == intent.type) {
                isForResult = false
                studio = Studio()
                var parts = ("" + intent?.clipData?.getItemAt(0)?.text).split("\n")
                studio.name = parts[0]
                studio.address = parts[1]
                studio.phone = parts[2]
                studio.link =
                    if (parts[3].contains("go.2gis.com")) parts[3] else if (parts[4].contains("go.2gis.com")) parts[4] else ""

            }
        }
        if (studio == null) {
            studio = Studio()
        } else {
            studioName.setText(studio.name)
            studioAddress.setText(studio.address)
            studioPhone.setText(studio.phone)
            for (room in studio.rooms) {
                addRoom(room)
            }
        }
        if (roomsViews.isEmpty()) {
            addRoom(null)
        }
        studioName.addTextChangedListener(TextListener(studioName))
        studioAddress.addTextChangedListener(TextListener(studioAddress))
        addRoom.setOnClickListener {
            addRoom(null)
        }
    }

    fun addRoom(photoRoom: PhotoRoom?) {
        var view = LayoutInflater.from(this).inflate(R.layout.item_room, roomsContainer, false)
        roomsViews.add(view)
        if (photoRoom != null) {
            view.roomName.setText(photoRoom.name)
            view.price.setText("" + photoRoom.price)
            view.priceWithDiscount.setText("" + photoRoom.priceWithDiscount)

        }
        view.roomName.addTextChangedListener(TextListener(view.roomName))
        view.delete.setOnClickListener {
            roomsContainer.removeView(view)
            roomsViews.remove(view)
        }
        view.price.setOnClickListener {
            var value = 0f
            try {
                value = view.price.text.toString().toFloat()
            } catch (nfe: NumberFormatException) {
                value = 500f
            }

            PricePickerDialog(this, getString(R.string.roomPrice), value)
                .setOnDoneListener {
                    view.price.setText(("" + it).replace(".0", ""))
                    paintBlack(view, R.id.price)
                    paintBlack(view, R.id.priceWithDiscount)
                }
                .show()
        }
        view.priceWithDiscount.setOnClickListener {
            var value = 0f
            try {
                value = view.price.text.toString().toFloat()
            } catch (nfe: NumberFormatException) {
                value = 500f
            }
            if (!MainPresenter.settings.useWheelsInput){
                PricePickerDialog(this, getString(R.string.roomPriceWithDiscount), value)
                    .setOnDoneListener {
                        view.priceWithDiscount.setText(("" + it).replace(".0", ""))
                        paintBlack(view, R.id.price)
                        paintBlack(view, R.id.priceWithDiscount)
                    }
                    .show()
            }else {
                SimpleDialog(this, SimpleDialog.DIALOG_TYPE.INPUT_ONLY)
                    .setTitle(getString(R.string.roomPriceWithDiscount))
                    .setMessage(""+value)
                    .setInputType(InputType.TYPE_CLASS_NUMBER)
                    .setOkListener { view.priceWithDiscount.setText(("" + it).replace(".0", ""))
                        paintBlack(view, R.id.price)
                        paintBlack(view, R.id.priceWithDiscount)
                    }
                    .build()

            }
        }
        roomsContainer.addView(view)
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
                if (studio == null) {
                    studio = Studio()
                }

                var studioNameString = studioName.text.toString()

                if (!studioNameString.equals("")) {
                    studio.name = studioNameString
                } else {
                    studioName.setBackgroundColor(resources.getColor(R.color.red))
                    return true
                }


                var studioAddressString = studioAddress.text.toString()
                if (!studioAddressString.equals("")) {
                    studio.address = studioAddressString
                } else {
                    studioAddress.setBackgroundColor(resources.getColor(R.color.red))
                    return true
                }
                studio.phone = studioPhone.text.toString()
                if (roomsViews.isEmpty()) {
                    Toast.makeText(this, "Добавьте хотя бы один зал", LENGTH_LONG).show()
                    return true
                }
                studio.cleanRooms()
                for (roomsView in roomsViews) {
                    val photoRoom = PhotoRoom()

                    var roomName =
                        (roomsView.findViewById(R.id.roomName) as EditText).text.toString()
                    if (roomName.equals("")) {
                        paintRedBack(roomsView, R.id.roomName)
                        return true
                    }
                    photoRoom.name = roomName
                    var price = getPrice(roomsView, R.id.price)
                    var priceWithDiscount = getPrice(roomsView, R.id.priceWithDiscount)

                    if (price == 0 && priceWithDiscount == 0) {
                        paintRedText(roomsView, R.id.price)
                        paintRedText(roomsView, R.id.priceWithDiscount)
                        return true
                    }

                    photoRoom.price = if (price != 0) price else priceWithDiscount
                    photoRoom.priceWithDiscount =
                        if (priceWithDiscount != 0) priceWithDiscount else price
                    studio.addRoom(photoRoom)
                }

                if (isForResult) {
                    val finishIntent = Intent()
                    finishIntent.putExtra("STUDIO", studio)
                    setResult(Activity.RESULT_OK, finishIntent)

                } else {
                    presenter.addStudio(studio)
                }
                finish()
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private fun paintRedBack(roomsView: View, view: Int) {
        (roomsView.findViewById(view) as TextView).setBackgroundColor(resources.getColor(R.color.red))
    }


    private fun getPrice(roomsView: View, price: Int): Int {
        try {
            return (roomsView.findViewById(price) as TextView).text.toString().toInt()
        } catch (nfe: NumberFormatException) {
            return 0
        }
    }

    private fun paintRedText(roomsView: View, viewId: Int) {
        (roomsView.findViewById(viewId) as TextView).setTextColor(resources.getColor(R.color.red))
    }

    private fun paintBlack(roomsView: View, viewId: Int) {
        (roomsView.findViewById(viewId) as TextView).setTextColor(resources.getColor(R.color.black))
    }

    inner class TextListener(val view: View) : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s != null && s.length > 0) {
                view.setBackgroundColor(resources.getColor(R.color.white))
            } else {
                view.setBackgroundColor(resources.getColor(R.color.red))
            }
        }

        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }


    }
}