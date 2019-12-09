package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 03.12.2019.
 */

@Entity
class Dress (var fileName : String, var comment : String = "", var price : Int = 0) : Serializable{
@PrimaryKey
var id :String= ""

    override fun equals(other: Any?): Boolean {
        return if (other is Dress && other.fileName.equals(fileName))true else super.equals(other)
    }
}