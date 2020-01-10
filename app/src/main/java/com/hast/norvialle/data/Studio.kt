package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hast.norvialle.repository.database.StudioDataConverter
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Entity
class Studio : Serializable{


    @PrimaryKey
    var id : String = ""
    var name : String = ""
    var address : String = ""
    var phone : String = ""
    var link: String = ""
    @TypeConverters(StudioDataConverter::class)
    var rooms : ArrayList<PhotoRoom> = ArrayList()

    fun addRoom(photoRoom: PhotoRoom) {



        rooms.add(photoRoom)
    }

    fun cleanRooms() {

            rooms.clear()

    }

    fun roomsContains(filterText: String): Boolean {
        for (room in rooms) {
            if (room.name.toLowerCase().contains(filterText.toLowerCase())){
                return true
            }
        }
        return false
    }
}