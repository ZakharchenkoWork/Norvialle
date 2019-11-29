package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.hast.norvialle.db.StudioDataConverter

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Entity
class Studio {
    @PrimaryKey
    var id : String = ""
    var name : String = ""
    var address : String = ""
    @TypeConverters(StudioDataConverter::class)
    lateinit var rooms : List<PhotoRoom>


}