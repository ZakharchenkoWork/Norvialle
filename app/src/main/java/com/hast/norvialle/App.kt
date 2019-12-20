package com.hast.norvialle

import android.app.Application
import androidx.room.Room

import com.hast.norvialle.db.AppDatabase


/**
 * Created by Konstantyn Zakharchenko on 28.11.2019.
 */

class App : Application() {
    companion object {
        lateinit var db : AppDatabase
    }


    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(this, AppDatabase::class.java, "database")
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .addMigrations(AppDatabase.MIGRATION_2_3)
            .addMigrations(AppDatabase.MIGRATION_3_4)
            .addMigrations(AppDatabase.MIGRATION_4_5)
            .addMigrations(AppDatabase.MIGRATION_5_6)
            .addMigrations(AppDatabase.MIGRATION_6_7)
            .addMigrations(AppDatabase.MIGRATION_7_8)
            .addMigrations(AppDatabase.MIGRATION_8_9)
            .addMigrations(AppDatabase.MIGRATION_9_10)
            .allowMainThreadQueries().build();

    }
}