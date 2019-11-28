package com.hast.norvialle.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.hast.norvialle.Event


/**
 * Created by Konstantyn Zakharchenko on 28.11.2019.
 */
@Database(entities = [Event::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventsDao?
}