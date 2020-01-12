package com.hast.norvialle.presenataion.base

import android.content.Intent
import android.provider.ContactsContract
import com.hast.norvialle.presenataion.base.assistent.AddAssistantActivity

/**
 * Created by Konstantyn Zakharchenko on 10.01.2020.
 */

fun BaseActivity.openContactsList(requestCode : Int) {
    val intent = Intent(Intent.ACTION_PICK)
    intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
    startActivityForResult(intent, requestCode)
}