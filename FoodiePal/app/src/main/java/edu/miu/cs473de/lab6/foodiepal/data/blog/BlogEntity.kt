package edu.miu.cs473de.lab6.foodiepal.data.blog

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import edu.miu.cs473de.lab6.foodiepal.data.user.User

@Entity(
    tableName = "Blog",
    indices = [
        Index(value = ["id"], unique = true),
        Index(value = ["authorId", "postedAt"], unique = true)
    ],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["authorId"])]
)
data class BlogEntity(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var title: String,
    var content: String,
    var authorId: Int,
    var postedAt: Long
)
