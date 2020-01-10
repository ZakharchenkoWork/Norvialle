package com.hast.norvialle.repository.database.dao

import androidx.room.*
import com.hast.norvialle.data.MakeupArtist

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