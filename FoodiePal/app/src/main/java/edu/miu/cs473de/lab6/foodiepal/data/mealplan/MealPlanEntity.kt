package edu.miu.cs473de.lab6.foodiepal.data.mealplan

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import edu.miu.cs473de.lab6.foodiepal.data.user.User
import java.time.LocalDate

@Entity(
    indices = [Index(value = ["date", "planner_id"], unique = true)],
    primaryKeys = ["date", "planner_id"],
    foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["planner_id"])])
data class MealPlanEntity(
    val date: Long,
    val plan: String,
    @ColumnInfo(name = "planner_id") val plannerId: Int
)
