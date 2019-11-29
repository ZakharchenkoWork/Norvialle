package com.hast.norvialle.db


import androidx.room.Database
import androidx.room.RoomDatabase
import com.hast.norvialle.Event


/**
 * Created by Konstantyn Zakharchenko on 28.11.2019.
 */
@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventsDao
}