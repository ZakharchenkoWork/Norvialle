package com.hast.norvialle.gui.makeup

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.gui.BaseActivity
import com.hast.norvialle.gui.MainPresenter
import com.hast.norvialle.utils.*
import kotlinx.android.synthetic.main.activity_add_makeup_artist.*
import kotlinx.android.synthetic.main.activity_add_makeup_artist.contactsList
import kotlinx.android.synthetic.main.activity_add_makeup_artist.phone


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AddMakeupArtistActivity : BaseActivity() {
    companion object {
        val EDIT: Int = 1
        val MAKEUP_ARTIST: String = "MAKEUP_ARTIST"
        val PICK_CONTACT: Int = 197
    }



    lateinit var makeupArtist: MakeupArtist

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_makeup_artist)
        setSupportActionBar(toolbar)
            makeupArtist = intent?.extras?.getSerializable(MAKEUP_ARTIST) as MakeupArtist

        if (makeupArtist == null) {
            makeupArtist = MakeupArtist("",0,"")
        } else {
            name.setText(makeupArtist.name)
            phone.setText(makeupArtist.phone)
            price.setText(""+makeupArtist.defaultPrice)

        }

        name.addTextChangedListener(TextListener(name))
        phone.addTextChangedListener(TextListener(phone))
        price.addTextChangedListener(TextListener(price))
        contactsList.setOnClickListener { openContactsList() }
        price.setOnClickListener {
            priceInputDialog(this, R.string.makeupPrice, getFloatValue(price)){
                price.setText(stringPriceWithPlaceholder(it, ""))
            }
        }
    }



    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                finish()
            }
            R.id.save -> {
                if (makeupArtist == null) {
                    makeupArtist = MakeupArtist("",0,"")
                }


                if (!name.text.equals("")) {
                    makeupArtist.name = name.text.toString()
                } else {
                    name.setBackgroundColor(resources.getColor(R.color.red))
                    return true
                }

                if (!phone.text.equals("")) {
                    makeupArtist.phone = phone.text.toString()
                } else {
                    phone.setBackgroundColor(resources.getColor(R.color.red))
                    return true
                }

                if (!price.text.equals("")) {
                    makeupArtist.defaultPrice = getIntValue(price)
                } else {
                    price.setBackgroundColor(resources.getColor(R.color.red))
                    return true
                }

                    MainPresenter.addMakeupArtist(makeupArtist)
                setResult(Activity.RESULT_OK)
                finish()
            }

        }
        return super.onOptionsItemSelected(item);
    }

    override fun getMenuRes(): Int {
      return R.menu.menu_save
    }

    override fun getMenuTitleRes(): Int {
      return R.string.adding_makeup_artist
    }

    private fun openContactsList() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = Phone.CONTENT_TYPE
        startActivityForResult(intent, PICK_CONTACT)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
           if (requestCode == PICK_CONTACT && data != null) {
               val contactUri = data?.getData();
               val projection = arrayOf(Phone.NUMBER, Phone.DISPLAY_NAME)
               val cursor: Cursor = contentResolver
                   .query(contactUri, projection, null, null, null)
               cursor.moveToFirst()

               val columnPhone: Int = cursor.getColumnIndex(Phone.NUMBER)
               val columnName: Int = cursor.getColumnIndex(Phone.DISPLAY_NAME)
               phone.setText(cursor.getString(columnPhone))
               name.setText(cursor.getString(columnName))
            }
        }
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