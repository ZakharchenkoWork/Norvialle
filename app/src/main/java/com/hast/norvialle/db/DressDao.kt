package com.hast.norvialle.db

import androidx.room.*
import com.hast.norvialle.data.Dress
import com.hast.norvialle.data.MakeupArtist

import com.hast.norvialle.data.Studio

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Dao
interface DressDao {
    @Query("SELECT * FROM Dress")
    fun getAll() : List<Dress>

    @Insert
    fun insert(dress : Dress)

    @Update
    fun update(dress : Dress)

    @Delete
    fun delete(dress : Dress)
}