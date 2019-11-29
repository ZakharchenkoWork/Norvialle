package com.hast.norvialle.db

import androidx.room.*

import com.hast.norvialle.data.Studio

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Dao
interface StudioDao {
    @Query("SELECT * FROM studio")
    fun getAll() : List<Studio>

    @Insert
    fun insert(event : Studio)

    @Update
    fun update(event : Studio)

    @Delete
    fun delete(event : Studio)
}