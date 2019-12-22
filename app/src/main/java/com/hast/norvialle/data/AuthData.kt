package com.hast.norvialle.data

import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 17.12.2019.
 */
class AuthData (var login : String, var password:String) : Serializable {
    var isPhotographer = false
    var isMakeupArtist = false
    var isAssistant = false
    fun isEmpty(): Boolean {
        return login.isEmpty() || password.isEmpty()
    }
}