package com.hast.norvialle.utils.data_validation

/**
 * Created by Konstantyn Zakharchenko on 23.12.2019.
 */
interface Validatable {
    var onStateChanged : (()->Unit)

   abstract fun validationCheck() : Boolean
    fun stateChanged(){
        onStateChanged.invoke()
    }
}