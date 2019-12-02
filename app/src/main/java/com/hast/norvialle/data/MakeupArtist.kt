package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 02.12.2019.
 */
@Entity
class MakeupArtist(var name: String, var defaultPrice:Int, var phone:String) : Serializable{
    @PrimaryKey
    var id = ""
}
