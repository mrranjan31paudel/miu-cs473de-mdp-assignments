package edu.miu.cs473de.lab6.gardeningjournal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import edu.miu.cs473de.lab6.gardeningjournal.db.daos.PlantDao
import edu.miu.cs473de.lab6.gardeningjournal.db.entities.Plant

@Database(
    entities = [Plant::class],
    version = 1,
    exportSchema = true
)
abstract class GardeningDatabase(): RoomDatabase() {
    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile private var INSTANCE: GardeningDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            GardeningDatabase::class.java,
            "gardeningdatabase"
        ).build()
    }
}