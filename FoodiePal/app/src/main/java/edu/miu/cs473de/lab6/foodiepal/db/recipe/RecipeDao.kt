package edu.miu.cs473de.lab6.foodiepal.db.recipe

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe

@Dao
interface RecipeDao {
    @Query("SELECT * FROM Recipe")
    suspend fun getAllRecipes(): List<Recipe>?

    @Query("SELECT * FROM Recipe WHERE author_id=:authorId")
    suspend fun getAllRecipesByAuthorId(authorId: Int): List<Recipe>?

    @Query("SELECT * FROM Recipe WHERE id=:id")
    suspend fun getRecipeById(id: Int): Recipe?

    @Query("SELECT COUNT(*) FROM Recipe")
    suspend fun count(): Int?

    @Insert
    suspend fun createRecipe(recipe: Recipe): Long

    @Insert
    suspend fun bulkInsertRecipe(recipes: ArrayList<Recipe>)

    @Update
    suspend fun updateRecipe(updatedRecipe: Recipe)

    @Delete
    suspend fun deleteRecipe(recipe: Recipe)
}