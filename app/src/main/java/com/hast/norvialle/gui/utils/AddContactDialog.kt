package com.hast.norvialle.gui.utils

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.WindowManager
import android.widget.EditText
import com.hast.norvialle.R
import com.hast.norvialle.gui.MainPresenter

/**
 * Created by Konstantyn Zakharchenko on 23.09.2019.
 */
class AddContactDialog : DialogFragment() {
    val presenter: MainPresenter = MainPresenter

    companion object {
        private const val EXTRA_TITLE = "title"
        private const val EXTRA_HINT = "hint"
        private const val EXTRA_MULTILINE = "multiline"
        private const val EXTRA_TEXT = "text"

        fun newInstance(
            title: String? = null,
            hint: String? = null,
            text: String? = null,
            isMultiline: Boolean = false
        ): AddContactDialog {
            val dialog = AddContactDialog()
            val args = Bundle().apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_HINT, hint)
                putString(EXTRA_TEXT, text)
                putBoolean(EXTRA_MULTILINE, isMultiline)
            }
            dialog.arguments = args
            return dialog
        }
    }

    lateinit var name: EditText
    lateinit var link: EditText
    var nameText: String = ""
    var linkText: String = ""
    var onOk: ((name: String, link: String) -> Unit)? = null
    var onCancel: (() -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val text: String? = arguments?.getString(EXTRA_TEXT)
        val isMiltiline = arguments?.getBoolean(EXTRA_MULTILINE) ?: false

        // StackOverflowError
        // val view = layoutInflater.inflate(R.layout.dialog_edit_text, null)
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_edit_text, null)

        name = view.findViewById(R.id.name)
        link = view.findViewById(R.id.link)
        name.setText(nameText)
        link.setText(linkText)

        if (isMiltiline) {
            name.minLines = 3
            //  editText.inputType = InputType.TYPE_TEXT_FLAG_MULTI_LINE or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }
        if (text != null) {
            name.append(text)
        }

        val builder = AlertDialog.Builder(context!!)
            .setTitle("Adding new contact")
            .setView(view)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                onOk?.invoke(name.text.toString(), link.text.toString())
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->
                onCancel?.invoke()
            }
        val dialog = builder.create()

        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        return dialog
    }

    fun composeContactData(name: String, link: String): String {
        return "<html><a href=\"" + link + "\">" + name + "</a></html>"
    }
}