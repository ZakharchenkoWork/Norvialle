package com.hast.norvialle

import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 28.09.2019.
 */
class Event(var id: String, var name: String, var time: Long) : Serializable{

    var avatar : String = ""
    var link : String = ""
    var description : String = ""
    var orderStudio : Boolean = false
    var orderDress : Boolean = false
    var orderMakeup : Boolean = false

    constructor() : this("", "",0) {

    }

    fun copy() : Event{
        val event = Event("", name, time)
        event.avatar = avatar;
        event.link  = link
        event.description = description
        return event
    }
}