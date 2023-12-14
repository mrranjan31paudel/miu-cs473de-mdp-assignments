package edu.miu.cs473de.lab6.foodiepal.data.recipe

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.miu.cs473de.lab6.foodiepal.data.user.User

@Entity(
    indices = [Index(value = ["id"], unique = true)],
    foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("author_id"))]
)
data class Recipe(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "img_src") var imgSrc: Int,
    var name: String,
    @ColumnInfo(name = "cooking_time_in_min") var cookingTimeInMin: Int,
    var rating: Float,
    var description: String,
    @ColumnInfo(name = "author_id") var authorId: Int
)
