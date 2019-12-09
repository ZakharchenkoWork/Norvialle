package com.hast.norvialle.utils

import android.text.Editable
import android.text.TextWatcher
import com.hast.norvialle.gui.studio.StudiosAdapter
import kotlinx.android.synthetic.main.activity_search_list.*

/**
 * Created by Konstantyn Zakharchenko on 07.12.2019.
 */
fun preparePhone(phone : String) : String{
    return phone.replace(" ", "").replace("(", "").replace(")", "").replace("-", "").replace("+", "").trim()
}
fun getSearchTextWatcher(listener: ((filterText : String)->Unit)): TextWatcher {
  return object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            listener.invoke(""+p0)
        }
    }
}