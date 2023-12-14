package edu.miu.cs473de.lab6.foodiepal.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe
import edu.miu.cs473de.lab6.foodiepal.data.user.User
import edu.miu.cs473de.lab6.foodiepal.db.recipe.RecipeDao
import edu.miu.cs473de.lab6.foodiepal.db.user.UserDao

@Database(
    entities = [User::class, Recipe::class],
    version = 1,
    exportSchema = true)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recipeDao(): RecipeDao
}