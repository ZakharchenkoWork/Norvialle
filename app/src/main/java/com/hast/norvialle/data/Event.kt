package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hast.norvialle.db.DressDataConverter
import com.hast.norvialle.db.StudioDataConverter
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 28.09.2019.
 */
@Entity
class Event(@PrimaryKey var id: String, var name: String, var time: Long) : Serializable {



    var contactPhone: String = ""
    var avatar: String = ""
    var link: String = ""
    var description: String = ""
    var orderStudio: Boolean = false
    var studioName = ""
    var studioAddress = ""
    var studioGeo = ""
    var studioRoom = ""
    var studioPhone = ""

    var orderDress: Boolean = false
    var orderMakeup: Boolean = false
    var makeupArtistName: String = ""
    var makeupPrice: Int = 0
    var makeupPhone: String = ""

    var makeupTime: Long = 0

    var orderAssistant: Boolean = false
    var assistantName: String = ""
    var assistantPrice: Int = 0
    var assistantPhone: String = ""

    var paidPrice: Int = 0
    var totalPrice: Int = 0
    var additionalId:Int = 0
    @TypeConverters(DressDataConverter::class)
    var dresses = ArrayList<Dress>()
@Ignore
    constructor() : this("", "", 0) {

    }

    fun getMoneyLeft(): Int {
        return totalPrice - paidPrice
    }
    fun copy(): Event {
        val event = Event("", name, time)
        event.avatar = avatar;
        event.link = link
        event.description = description
        event.contactPhone = contactPhone
        event.orderStudio= orderStudio
        event.studioName = studioName
        event.studioAddress = studioAddress
        event.studioGeo = studioGeo
        event.studioRoom = studioRoom
        event.orderDress = orderDress
        event.orderMakeup = orderMakeup
        event.paidPrice =paidPrice
        event.totalPrice = totalPrice

        return event
    }


    override fun equals(other: Any?): Boolean {
        return if (other is Event) id.equals(other.id) else super.equals(other)
    }
}