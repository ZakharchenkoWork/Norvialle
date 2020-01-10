package com.hast.norvialle.repository.database


import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hast.norvialle.data.*
import com.hast.norvialle.repository.database.dao.*


/**
 * Created by Konstantyn Zakharchenko on 28.11.2019.
 */
@Database(entities = [Event::class, Studio::class, MakeupArtist::class, Dress::class, Settings::class, Contact::class, Assistant::class],
    version = 10)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        var migrationFrom6to7 = false
        var migrateFrom4to5 = false
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

        val MIGRATION_2_3: Migration? = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS Dress (`id` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("ALTER TABLE Dress ADD COLUMN fileName TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE Dress ADD COLUMN price INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE Dress ADD COLUMN comment TEXT DEFAULT '' NOT NULL")
            }

        }

        val MIGRATION_3_4: Migration? = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Event ADD COLUMN dresses TEXT DEFAULT '' NOT NULL")

            }

        }
        val MIGRATION_4_5: Migration? = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS Settings (`id` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("ALTER TABLE Settings ADD COLUMN notifyDayBefore INTEGER DEFAULT  1 NOT NULL")
                database.execSQL("ALTER TABLE Settings ADD COLUMN timeOfDayBefore INTEGER DEFAULT  79200000 NOT NULL")
                database.execSQL("ALTER TABLE Settings ADD COLUMN notifySameDay INTEGER DEFAULT  1 NOT NULL")
                database.execSQL("ALTER TABLE Settings ADD COLUMN timeOfSameDay INTEGER DEFAULT  28800000 NOT NULL")
                database.execSQL("ALTER TABLE Settings ADD COLUMN notifyTimeBefore INTEGER DEFAULT  1 NOT NULL")
                database.execSQL("ALTER TABLE Settings ADD COLUMN timeBeforeShoot INTEGER DEFAULT 120000000 NOT NULL")
                migrateFrom4to5 = true

            }

        }
        val MIGRATION_5_6: Migration? = object : Migration(5, 6) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS Contact (`id` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("ALTER TABLE Contact ADD COLUMN name TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE Contact ADD COLUMN link TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE Contact ADD COLUMN phone TEXT DEFAULT '' NOT NULL")
            }

        }

        val MIGRATION_6_7: Migration? = object : Migration(6, 7) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("ALTER TABLE Event ADD COLUMN additionalId INTEGER DEFAULT 555 NOT NULL")
                migrationFrom6to7 = true
            }
        }
            val MIGRATION_7_8: Migration? = object : Migration(7, 8) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("ALTER TABLE Settings ADD COLUMN useWheelsInput INTEGER DEFAULT  1 NOT NULL")
            }

        }

        val MIGRATION_8_9: Migration? = object : Migration(8, 9) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS Assistant (`id` TEXT NOT NULL, PRIMARY KEY(`id`))")
                database.execSQL("ALTER TABLE Assistant ADD COLUMN name TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE Assistant ADD COLUMN defaultPrice INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE Assistant ADD COLUMN phone TEXT DEFAULT '' NOT NULL")

                database.execSQL("ALTER TABLE Event ADD COLUMN orderAssistant INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE Event ADD COLUMN assistantName TEXT DEFAULT '' NOT NULL")
                database.execSQL("ALTER TABLE Event ADD COLUMN assistantPrice INTEGER DEFAULT 0 NOT NULL")
                database.execSQL("ALTER TABLE Event ADD COLUMN assistantPhone TEXT DEFAULT '' NOT NULL")

            }
        }

        val MIGRATION_9_10: Migration? = object : Migration(9, 10) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE Settings ADD COLUMN useFingerprint INTEGER DEFAULT 0 NOT NULL")

            }
        }
    }

    abstract fun eventDao(): EventsDao
    abstract fun studioDao(): StudioDao
    abstract fun makeupDao(): MakeupDao
    abstract fun dressDao(): DressDao
    abstract fun settingsDao(): SettingsDao
    abstract fun contactsDao(): ContactDao
    abstract fun assistentDao(): AssistentDao
}