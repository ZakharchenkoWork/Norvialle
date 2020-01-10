package com.hast.norvialle.repository.database.dao

import androidx.room.*
import com.hast.norvialle.data.Settings

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Dao
interface SettingsDao {
    @Query("SELECT * FROM Settings WHERE id = 'settings'")
    fun get() : Settings

    @Insert
    fun insert(settings : Settings)
    @Update
    fun update(settings : Settings)

}