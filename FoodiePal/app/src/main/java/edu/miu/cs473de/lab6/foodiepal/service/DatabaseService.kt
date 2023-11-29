package edu.miu.cs473de.lab6.foodiepal.service

import android.content.Context
import androidx.room.Room
import edu.miu.cs473de.lab6.foodiepal.db.AppDatabase

class DatabaseService {
    companion object {
        var db: AppDatabase? = null

        fun initDb(applicationContext: Context) {
            db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "foodiepal").build()
        }
    }
}