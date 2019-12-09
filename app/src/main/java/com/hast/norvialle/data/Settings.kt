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
    var notifyDayBefore = true
    var timeOfDayBefore :Long = 22*60*60*1000
    var notifySameDay = true
    var timeOfSameDay :Long= 8*60*60*1000
    var notifyTimeBefore = true
    var timeBeforeShoot :Long= 2*60*1000
}
