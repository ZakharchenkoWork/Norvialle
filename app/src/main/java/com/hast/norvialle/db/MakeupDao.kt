package com.hast.norvialle.db

import androidx.room.*
import com.hast.norvialle.data.MakeupArtist

import com.hast.norvialle.data.Studio

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Dao
interface MakeupDao {
    @Query("SELECT * FROM MakeupArtist")
    fun getAll() : List<MakeupArtist>

    @Insert
    fun insert(event : MakeupArtist)

    @Update
    fun update(event : MakeupArtist)

    @Delete
    fun delete(event : MakeupArtist)
}