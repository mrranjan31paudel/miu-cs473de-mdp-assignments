package edu.miu.cs473de.lab6.foodiepal.db.recipe

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe
import edu.miu.cs473de.lab6.foodiepal.data.recipe.RecipeEntity

@Dao
interface RecipeDao {
    @Query("SELECT r.id AS id, r.img_src as imgSrc, r.name AS name, r.cooking_time_in_min AS cookingTimeInMin, r.rating AS rating, r.description AS description, r.author_id AS authorId, (u.first_name || ' ' || u.last_name) AS authorName FROM Recipe r JOIN User u ON r.author_id = u.id")
    suspend fun getAllRecipes(): List<Recipe>?

    @Query("SELECT r.id AS id, r.img_src as imgSrc, r.name AS name, r.cooking_time_in_min AS cookingTimeInMin, r.rating AS rating, r.description AS description, r.author_id AS authorId FROM Recipe r WHERE author_id=:authorId")
    suspend fun getAllRecipesByAuthorId(authorId: Int): List<Recipe>?

    @Query("SELECT r.id AS id, r.img_src as imgSrc, r.name AS name, r.cooking_time_in_min AS cookingTimeInMin, r.rating AS rating, r.description AS description, r.author_id AS authorId, (u.first_name || ' ' || u.last_name) AS authorName FROM Recipe r JOIN User u ON r.author_id = u.id WHERE r.id=:recipeId")
    suspend fun getRecipeById(recipeId: Int): Recipe?

    @Query("SELECT COUNT(*) FROM Recipe")
    suspend fun count(): Int?

    @Insert
    suspend fun createRecipe(recipe: RecipeEntity): Long

    @Insert
    suspend fun bulkInsertRecipe(vararg recipe: RecipeEntity)

    @Update
    suspend fun updateRecipe(updatedRecipe: RecipeEntity)

    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
}