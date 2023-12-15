package edu.miu.cs473de.lab6.foodiepal.data.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.miu.cs473de.lab6.foodiepal.data.user.User

data class Recipe(
    val id: Int,
    var imgSrc: Int,
    var name: String,
    var cookingTimeInMin: Int,
    var rating: Float,
    var description: String,
    var authorId: Int,
    var authorName: String? = null
) {
    fun toEntity(): RecipeEntity {
        return RecipeEntity(id, imgSrc, name, cookingTimeInMin, rating, description, authorId)
    }
}
