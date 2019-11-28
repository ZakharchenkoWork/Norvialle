package com.hast.norvialle

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 28.09.2019.
 */
@Entity
class Event(@PrimaryKey var id: String, var name: String, var time: Long) : Serializable{

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