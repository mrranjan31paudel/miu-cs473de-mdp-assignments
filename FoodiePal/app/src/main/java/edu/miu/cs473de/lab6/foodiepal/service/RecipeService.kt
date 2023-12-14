package edu.miu.cs473de.lab6.foodiepal.service

import android.util.Patterns
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import kotlinx.coroutines.runBlocking

class RecipeService {

    companion object {
        private val NAME_REGEX = Regex("^[a-zA-Z ]{2,50}$")
        private val MINUTES_REGEX = Regex("^[1-9][0-9]{0,3}$")
        private val RATING_REGEX = Regex("^[0-5]$")
        private const val MAX_COOKING_TIME = 1440
        private fun validateNewRecipeData(name: String, cookingTime: String, rating: String, description: String) {
            if (name.isEmpty()) {
                throw ValidationException("newRecipeName", "Required with at least 2 characters and at most 5 characters!")
            }
            if (!name.matches(NAME_REGEX)) {
                throw ValidationException("newRecipeName", "Only alphabets allowed!")
            }
            if (cookingTime.isEmpty()) {
                throw ValidationException("newRecipeCookingTime", "Required!")
            }
            if (!cookingTime.matches(MINUTES_REGEX) || cookingTime.toInt() > MAX_COOKING_TIME) {
                throw ValidationException("newRecipeCookingTime", "Minimum allowed value is 1 minute and maximum is 1440 minutes")
            }
            if (rating.isEmpty() || !rating.matches(RATING_REGEX)) {
                throw ValidationException("newRecipeRating", "Required!")
            }
            if (description.isEmpty() || description.length < 5 || description.length > 500) {
                throw ValidationException("newRecipeDescription", "Required with at least 5 characters and at most 500 characters!")
            }
        }

        fun createRecipe(name: String, cookingTime: String, rating: String, description: String, authorId: Int): Int {
            val trimmedName = name.trim()
            val trimmedRating = rating.trim()
            val trimmedDescription = description.trim()
            validateNewRecipeData(trimmedName, cookingTime, trimmedRating, trimmedDescription)

            val recipeDao = DatabaseService.db?.recipeDao()
            val recipe = Recipe(0, 0, trimmedName, cookingTime.toInt(), trimmedRating.toFloat(), trimmedDescription, authorId)
            val newRecipeId = runBlocking {
                return@runBlocking recipeDao?.createRecipe(recipe)
            }

            return (newRecipeId ?: 0L).toInt()
        }

        fun getCount(): Int {
            val recipeDao = DatabaseService.db?.recipeDao()
            val count = runBlocking {
                return@runBlocking recipeDao?.count()
            }
            return count ?: 0;
        }

        fun getAllRecipes(): List<Recipe> {
            val recipeDao = DatabaseService.db?.recipeDao()
            val recipes = runBlocking {
                return@runBlocking recipeDao?.getAllRecipes()
            }
            return recipes ?: listOf()
        }

        fun getRecipeById(id: Int): Recipe? {
            val recipeDao = DatabaseService.db?.recipeDao()
            val recipe = runBlocking {
                return@runBlocking recipeDao?.getRecipeById(id)
            }
            return recipe
        }

        fun createManyRecipes(recipes: ArrayList<Recipe>) {
            val recipeDao = DatabaseService.db?.recipeDao()
            runBlocking {
                recipeDao?.bulkInsertRecipe(recipes)
            }
        }
    }
}