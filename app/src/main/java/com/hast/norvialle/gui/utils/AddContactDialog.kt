package com.hast.norvialle.gui.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle

import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.hast.norvialle.R
import com.hast.norvialle.data.Contact
import com.hast.norvialle.gui.MainPresenter
import kotlinx.android.synthetic.main.dialog_edit_text.*
import kotlinx.android.synthetic.main.dialog_edit_text.view.*

/**
 * Created by Konstantyn Zakharchenko on 23.09.2019.
 */
class AddContactDialog : DialogFragment() {
    val presenter: MainPresenter = MainPresenter

    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_HINT = "hint"
        private const val EXTRA_MULTILINE = "multiline"
        private const val EXTRA_NAME = "name"
        private const val EXTRA_LINK = "link"
        private const val EXTRA_PHONE = "phone"

        fun newInstance(): AddContactDialog {
            val dialog = AddContactDialog()
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

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        // StackOverflowError
        // val view = layoutInflater.inflate(R.layout.dialog_edit_text, null)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_edit_text, null)


        view.name.setText(nameText)
        view.link.setText(linkText)
        view.phone.setText(phoneText)

        val builder = AlertDialog.Builder(context!!)
            .setTitle("Adding new contact")
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onOk?.invoke(Contact(view.name.text.toString(), view.link.text.toString(), view.phone.text.toString()))
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                onCancel?.invoke()
            }
        val dialog = builder.create()

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        return dialog
    }


}