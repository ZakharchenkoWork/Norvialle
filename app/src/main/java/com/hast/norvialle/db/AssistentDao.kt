package com.hast.norvialle.db

import androidx.room.*
import com.hast.norvialle.data.Assistant

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Dao
interface AssistentDao {
    @Query("SELECT * FROM Assistant")
    fun getAll() : List<Assistant>

    @Insert
    fun insert(event : Assistant)

    @Update
    fun update(event : Assistant)

    @Delete
    fun delete(event : Assistant)
}