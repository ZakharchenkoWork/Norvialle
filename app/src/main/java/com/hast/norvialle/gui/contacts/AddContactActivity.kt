package com.hast.norvialle.gui.contacts

import android.app.Activity
import android.content.ClipData
import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hast.norvialle.R
import com.hast.norvialle.data.Contact
import com.hast.norvialle.gui.MainPresenter
import kotlinx.android.synthetic.main.activity_add_contact.*


/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
class AddContactActivity : AppCompatActivity() {
    companion object {
        val EDIT: Int = 111
        val DATA_TYPE = "CONTACT"
        val PICK_CONTACT: Int = 911
    }



    lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_contact)
        setSupportActionBar(toolbar)
        var actionBar = getSupportActionBar()
        if (actionBar != null) {
            getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar()?.setHomeAsUpIndicator(R.drawable.back);
            getSupportActionBar()?.setTitle(R.string.adding_contact);
        }

        contact = intent?.extras?.getSerializable(DATA_TYPE) as Contact

        if (contact == null) {
            contact = Contact("","","")
        } else {
            name.setText(contact.name)
            phone.setText(contact.phone)
            link.setText(contact.link)

        }

        paste.setOnClickListener{
            val clipboard: ClipboardManager =
                getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            val primaryClipDescription = clipboard.getPrimaryClipDescription()
            if (!clipboard.hasPrimaryClip() || primaryClipDescription == null || !clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN)) {
                Toast.makeText(this, getString(R.string.insta_hint), Toast.LENGTH_SHORT).show()
            } else {
                val primaryClip = clipboard.getPrimaryClip()
                if (primaryClip != null) {
                    val item: ClipData.Item = primaryClip.getItemAt(0)
                    link.setText(item.text.toString())
                }
            }
        }

        contactsList.setOnClickListener { openContactsList() }
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
                if (contact == null) {
                    contact = Contact("","","")
                }


                if (name.validationCheck()) {
                    contact.name = name.getText()
                } else {
                    return true
                }
                    contact.phone = phone.getText()
                    contact.link= link.getText()


                    MainPresenter.addContact(contact)
                finish()
            }

        }
        return super.onOptionsItemSelected(item);
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

}