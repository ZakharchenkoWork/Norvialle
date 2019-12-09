package com.hast.norvialle.utils

import android.content.Context
import androidx.annotation.StringRes
import com.hast.norvialle.R
import com.hast.norvialle.gui.dialogs.SimpleDialog

/**
 * Created by Konstantyn Zakharchenko on 09.12.2019.
 */
fun showDeleteDialog(context : Context, @StringRes itemName : Int, onDeleteListener: ()->Unit){
    SimpleDialog(context, SimpleDialog.DIALOG_TYPE.MESSAGE_ONLY)
        .setTitle(context.getString(R.string.delete_dialog_title))
        .setMessage(context.getString(R.string.delete_dialog_message, context.getString(itemName)))
        .setOkListener { onDeleteListener.invoke()  }
        .setOkButtonText(context.getString(R.string.delete_dialog_yes))
        .setCancelButtonText(context.getString(R.string.delete_dialog_no))
        .setCancelable(true)
        .build()
}