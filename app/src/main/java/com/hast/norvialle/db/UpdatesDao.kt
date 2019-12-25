package com.hast.norvialle.db

import androidx.room.*
import com.hast.norvialle.data.Contact
import com.hast.norvialle.data.MakeupArtist

import com.hast.norvialle.data.Studio
import com.hast.norvialle.data.server.UpdateData

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Dao
interface UpdatesDao {
    @Query("SELECT * FROM UpdateData")
    fun getAll() : List<UpdateData>

    @Insert
    fun insert(contact : UpdateData)


}