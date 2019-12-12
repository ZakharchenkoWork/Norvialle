package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Konstantyn Zakharchenko on 05.12.2019.
 */
@Entity
class Settings {
    @PrimaryKey
    var id = "settings"
    var notifyDayBefore = false
    var timeOfDayBefore :Long = 0
    var notifySameDay = false
    var timeOfSameDay :Long=0
    var notifyTimeBefore = false
    var timeBeforeShoot :Long= 0
    var useWheelsInput = false

    fun setDefault(){
        id = "settings"
        notifyDayBefore = true
        timeOfDayBefore  = 22*60*60*1000
        notifySameDay = true
        timeOfSameDay = 8*60*60*1000
        notifyTimeBefore = true
        timeBeforeShoot = 8*60*60*1000
        useWheelsInput = true
    }
}
