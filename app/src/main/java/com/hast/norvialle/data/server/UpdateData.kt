package com.hast.norvialle.data.server

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Konstantyn Zakharchenko on 24.12.2019.
 */
@Entity
class UpdateData(@PrimaryKey var time : Long = System.currentTimeMillis(), var dataType : DATA_TYPE, var actionType: ACTION_TYPE, var itemId : String){
    override fun equals(other: Any?): Boolean {
        if ( other is UpdateData){
            return time == other.time
        } else {
            return super.equals(other)
        }
    }
}

enum class DATA_TYPE{
    EVENT,
    STUDIO,
    CONTACT,
    DRESS,
    MAKEUP_ARTIST,
    ASSISTANT
}
enum class ACTION_TYPE{
    ADD,
    EDIT,
    REMOVE
}