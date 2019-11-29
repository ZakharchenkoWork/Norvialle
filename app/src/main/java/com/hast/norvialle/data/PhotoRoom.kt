package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Entity
class PhotoRoom {
    @PrimaryKey
    var id : String = ""
    var name : String = ""
    var price : Int = 0

}