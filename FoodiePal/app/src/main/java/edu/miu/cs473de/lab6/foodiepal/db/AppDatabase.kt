package edu.miu.cs473de.lab6.foodiepal.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import edu.miu.cs473de.lab6.foodiepal.data.aboutme.AboutMeItem
import edu.miu.cs473de.lab6.foodiepal.data.blog.BlogEntity
import edu.miu.cs473de.lab6.foodiepal.data.mealplan.MealPlanEntity
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe
import edu.miu.cs473de.lab6.foodiepal.data.recipe.RecipeEntity
import edu.miu.cs473de.lab6.foodiepal.data.user.User
import edu.miu.cs473de.lab6.foodiepal.db.aboutme.AboutMeDao
import edu.miu.cs473de.lab6.foodiepal.db.blog.BlogDao
import edu.miu.cs473de.lab6.foodiepal.db.mealplan.MealPlanDao
import edu.miu.cs473de.lab6.foodiepal.db.recipe.RecipeDao
import edu.miu.cs473de.lab6.foodiepal.db.user.UserDao

@Database(
    entities = [User::class, RecipeEntity::class, MealPlanEntity::class, BlogEntity::class, AboutMeItem::class],
    version = 1,
    exportSchema = true)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao(): MealPlanDao
    abstract fun blogDao(): BlogDao
    abstract fun aboutMeDao(): AboutMeDao
}