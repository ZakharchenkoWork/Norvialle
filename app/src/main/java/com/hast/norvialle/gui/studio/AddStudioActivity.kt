package com.hast.norvialle.gui.studio

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import com.hast.norvialle.gui.BaseActivity
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.utils.extractFromFloat
import com.hast.norvialle.utils.priceInputDialog
import kotlinx.android.synthetic.main.activity_add_studio.*
import kotlinx.android.synthetic.main.item_room.view.*

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AddStudioActivity : BaseActivity() {
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

        if (intent?.action != Intent.ACTION_SEND) {
            studio = intent?.extras?.getSerializable(STUDIO) as Studio
        } else {
            //TODO: Add support for Google Maps, Yandex and e.t.c.
            Log.d("Shared location", "" + intent?.clipData)
            if ("text/plain" == intent.type) {
                isForResult = false
                studio = Studio()
                var parts = ("" + intent?.clipData?.getItemAt(0)?.text).split("\n")
                studio.name = parts[0]
                studio.address = parts[1]
                studio.phone = parts[2]
                studio.link =
                    if (checkHasMapLink(parts[3])) parts[3] else if (checkHasMapLink(parts[4])) parts[4] else ""

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


        addRoom.setOnClickListener {
            addRoom(null)
            hideKeyboard()
        }
    }
fun checkHasMapLink(text : String) : Boolean{
        return (text.contains("go.2gis.com") || text.contains("maps.app.goo.gl"))
    }
    fun addRoom(photoRoom: PhotoRoom?) {
        var view = LayoutInflater.from(this).inflate(R.layout.item_room, roomsContainer, false)
        roomsViews.add(view)
        if (photoRoom != null) {
            view.roomName.setText(photoRoom.name)
            view.price.setText("" + photoRoom.price)
            view.priceWithDiscount.setText("" + photoRoom.priceWithDiscount)

        }

        view.delete.setOnClickListener {
            roomsContainer.removeView(view)
            roomsViews.remove(view)
        }
        view.price.setOnClickListener {
            val value = extractStartValue(view.price, view.priceWithDiscount)

            priceInputDialog(this, R.string.roomPrice, value){
                if (!it.equals("") && !it.equals("0") ) {
                    view.price.setText(extractFromFloat(it))
                    paintBlack(view, R.id.price)
                    paintBlack(view, R.id.priceWithDiscount)
                } else{
                    view.price.setText(R.string.price)
                }

            }
        }
        view.priceWithDiscount.setOnClickListener {
            val value = extractStartValue(view.priceWithDiscount, view.price)
            priceInputDialog(this, R.string.roomPriceWithDiscount, value){
                if (!it.equals("") && !it.equals("0")){
                    view.priceWithDiscount.setText(extractFromFloat(it))
                    paintBlack(view, R.id.price)
                    paintBlack(view, R.id.priceWithDiscount)
                } else{
                    view.priceWithDiscount.setText(R.string.discount)
                }

            }
        }
        roomsContainer.addView(view)
    }
    fun extractStartValue(view : TextView, failbackView : TextView? = null): Float {
        var value = 0f
        try {
            value = view.text.toString().toFloat()
        } catch (nfe: NumberFormatException) {
        }
        if (value == 0f){
            try {
                value = failbackView?.text.toString().toFloat()
            } catch (nfe: NumberFormatException) {
            }
        }
        return value
    }


    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.save -> {
                try{
                if (studio == null) {
                    studio = Studio()
                }

                    studio.name = checkToSave(studioName)
                    studio.address = checkToSave(studioAddress)
                    studio.phone = checkToSave(studioPhone)

                if (roomsViews.isEmpty()) {
                    throw InvalidSavingData(getString(R.string.no_rooms_errors))
                }

                studio.cleanRooms()
                for (roomsView in roomsViews) {
                    val photoRoom = PhotoRoom()

                    photoRoom.name = checkToSave(roomsView.roomName)

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
                } catch (dataCheckFailed : InvalidSavingData){
                    fastToast(dataCheckFailed)
                    return true
                }
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

    override fun getMenuRes(): Int {
        return R.menu.menu_save
    }

    override fun getMenuTitleRes(): Int {
        return R.string.add_studio
    }

}