package com.hast.norvialle.db

import androidx.room.*
import com.hast.norvialle.data.Dress
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.data.Settings

import com.hast.norvialle.data.Studio

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