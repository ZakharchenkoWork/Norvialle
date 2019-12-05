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
            .allowMainThreadQueries().build();

    }
}