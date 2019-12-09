package com.hast.norvialle.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by Konstantyn Zakharchenko on 01.12.2019.
 */


@Entity class Contact (var name : String, var link : String, var phone : String) : Serializable{
    @PrimaryKey var id = ""
}
