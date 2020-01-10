package com.hast.norvialle.presenataion.utils.dialogs

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.view.View

import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.hast.norvialle.R
import com.hast.norvialle.data.Contact
import com.hast.norvialle.presenataion.base.contacts.AddContactActivity
import kotlinx.android.synthetic.main.dialog_edit_text.view.*

/**
 * Created by Konstantyn Zakharchenko on 23.09.2019.
 */
class AddContactDialog : DialogFragment() {

    companion object {
        fun newInstance(): AddContactDialog {
            val dialog =
                AddContactDialog()
            return dialog
        }
    }

    lateinit var name: EditText
    lateinit var link: EditText
    var nameText: String = ""
    var linkText: String = ""
    var phoneText: String = ""
    var onOk: ((contact: Contact) -> Unit)? = null
    var onCancel: (() -> Unit)? = null
    lateinit var dialogView : View
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_edit_text, null)


        dialogView.name.setText(nameText)
        dialogView.link.setText(linkText)
        dialogView.phone.setText(phoneText)
        dialogView.contactsList.setOnClickListener { openContactsList() }

        val builder = AlertDialog.Builder(context!!)
            .setTitle("Adding new contact")
            .setView(dialogView)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onOk?.invoke(Contact(dialogView.name.text.toString(), dialogView.link.text.toString(), dialogView.phone.text.toString()))
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                onCancel?.invoke()
            }
        val dialog = builder.create()

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        return dialog
    }

    private fun openContactsList() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
        (context as Activity).startActivityForResult(intent, AddContactActivity.PICK_CONTACT)
    }

    fun update() {
        if(::dialogView.isInitialized)
        dialogView.name.setText(nameText)
        dialogView.phone.setText(phoneText)
    }
}