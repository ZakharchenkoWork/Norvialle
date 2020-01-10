package com.hast.norvialle.presenataion.utils.dialogs

import android.content.Context
import android.text.InputType
import androidx.annotation.StringRes
import com.hast.norvialle.R
import com.hast.norvialle.domain.MainPresenter
import com.hast.norvialle.utils.extractFromFloat

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

fun priceInputDialog(context : Context, @StringRes title : Int, startValue : Float, onDone : ((result : String) -> Unit)){
    if (MainPresenter.settings.useWheelsInput){
        PricePickerDialog(context,context.getString(title), startValue)
            .setOnDoneListener {
                onDone(extractFromFloat(it))
            }
            .show()
    }else {
        SimpleDialog(context, SimpleDialog.DIALOG_TYPE.INPUT_ONLY)
            .setTitle(context.getString(title))
            .setEditTextDefaultData(
                extractFromFloat(
                    startValue
                )
            )
            .setInputType(InputType.TYPE_CLASS_NUMBER)
            .setOkListener {
                onDone(extractFromFloat(it))
            }
            .build()
    }
}