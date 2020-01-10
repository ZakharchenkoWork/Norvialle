package com.hast.norvialle.repository.database.dao

import androidx.room.*
import com.hast.norvialle.data.Contact

/**
 * Created by Konstantyn Zakharchenko on 29.11.2019.
 */
@Dao
interface ContactDao {
    @Query("SELECT * FROM Contact")
    fun getAll() : List<Contact>

    @Insert
    fun insert(contact : Contact)

    @Update
    fun update(contact : Contact)

    @Delete
    fun delete(contact : Contact)
}