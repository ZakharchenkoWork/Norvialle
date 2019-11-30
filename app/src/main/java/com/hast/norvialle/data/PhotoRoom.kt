package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */

class PhotoRoom : Serializable{

    var name : String = ""
    var price : Int = 0
    var priceWithDiscount : Int = 0

}