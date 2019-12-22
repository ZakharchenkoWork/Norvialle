package com.hast.norvialle.utils

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.EditText
import java.lang.NumberFormatException

/**
 * Created by Konstantyn Zakharchenko on 07.12.2019.
 */

fun getSearchTextWatcher(listener: ((filterText: String) -> Unit)): TextWatcher {
    return object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            listener.invoke("" + p0)
        }
    }
}

fun getNonZeroTextWatcher(
    editText: EditText,
    listener: ((filterText: String) -> Unit)
): TextWatcher {
    return object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
        }

        override fun beforeTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
            if (text.toString().equals("0")) {
                editText.setText("")
            }
        }

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            listener.invoke("" + text)
        }
    }
}
fun getEmailWatcher(
    listener: ((isValidEmail: Boolean) -> Unit)
): TextWatcher {
    return object : TextWatcher {
        override fun afterTextChanged(text: Editable?) {
        }

        override fun beforeTextChanged(text: CharSequence, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            listener.invoke(!TextUtils.isEmpty(text) && android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches())
        }
    }

}

