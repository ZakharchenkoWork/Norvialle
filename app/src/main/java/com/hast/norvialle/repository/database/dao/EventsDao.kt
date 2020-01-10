package com.hast.norvialle.repository.database.dao


import androidx.room.*
import com.hast.norvialle.data.Event
import io.reactivex.Flowable

/**
 * Created by Konstantyn Zakharchenko on 28.11.2019.
 */

@Dao
interface EventsDao {

    @Query("SELECT * FROM event")
    fun getAll() : List<Event>

    @Query("SELECT * FROM event WHERE time > :dayNow")
    fun getNextList(dayNow: Long) : Flowable<List<Event>>

    @Query("SELECT * FROM event WHERE id = :id")
    fun getById(id : Long): Event

    @Insert
    fun insert(event : Event)

    @Update
    fun update(event : Event)

    @Delete
    fun delete(event : Event)

}