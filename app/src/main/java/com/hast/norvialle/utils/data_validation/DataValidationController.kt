package com.hast.norvialle.utils.data_validation

/**
 * Created by Konstantyn Zakharchenko on 23.12.2019.
 */
class DataValidationController {
    val items = ArrayList<Validatable>()
    var onStateChanged : ((isValid : Boolean)->Unit) = {}
    fun add(item : Validatable){
        items.add(item)
        item.onStateChanged = {
            isAllValid()
        }
    }
    fun isAllValid():Boolean{
        for (item in items){
            if (!item.validationCheck()){
                onStateChanged(false)
                return false
            }
        }
        onStateChanged(true)
        return true
    }


}