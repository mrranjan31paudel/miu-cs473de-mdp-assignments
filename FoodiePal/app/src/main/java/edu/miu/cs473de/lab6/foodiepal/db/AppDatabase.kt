package edu.miu.cs473de.lab6.foodiepal.db

import androidx.room.Database
import androidx.room.RoomDatabase
import edu.miu.cs473de.lab6.foodiepal.data.user.User
import edu.miu.cs473de.lab6.foodiepal.db.user.UserDao

@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
}