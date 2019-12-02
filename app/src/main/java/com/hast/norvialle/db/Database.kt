package com.hast.norvialle.db


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hast.norvialle.data.Event
import com.hast.norvialle.data.MakeupArtist
import com.hast.norvialle.data.Studio


/**
 * Created by Konstantyn Zakharchenko on 28.11.2019.
 */
@Database(entities = [Event::class, Studio::class, MakeupArtist::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    companion object {

        val MIGRATION_1_2: Migration? = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Event ADD COLUMN makeupArtistName TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE Event ADD COLUMN makeupPhone TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE Event ADD COLUMN makeupTime INTEGER DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE Event ADD COLUMN makeupPrice INTEGER DEFAULT '' NOT NULL")
                database.execSQL("CREATE TABLE IF NOT EXISTS MakeupArtist (`id` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("ALTER TABLE MakeupArtist ADD COLUMN name TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE MakeupArtist ADD COLUMN defaultPrice INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE MakeupArtist ADD COLUMN phone TEXT DEFAULT '' NOT NULL")
            }

        }
    }

    abstract fun eventDao(): EventsDao
    abstract fun studioDao(): StudioDao
    abstract fun makeupDao(): MakeupDao
}