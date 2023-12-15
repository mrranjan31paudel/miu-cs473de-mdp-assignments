package edu.miu.cs473de.lab6.foodiepal.db.mealplan

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.miu.cs473de.lab6.foodiepal.data.mealplan.MealPlanEntity

@Dao
interface MealPlanDao {
    @Query("SELECT * FROM MealPlanEntity WHERE planner_id=:plannerId AND CAST(strftime('%m', date, 'unixepoch') AS INTEGER) =:month AND CAST(strftime('%Y', date, 'unixepoch') AS INTEGER) =:year")
    suspend fun getMealPlansByPlannerIdAndMonthAndYear(plannerId: Int, month: Int, year: Int): List<MealPlanEntity>?

    @Insert
    suspend fun createMealPlan(mealPlan: MealPlanEntity)

    @Update
    suspend fun updateMealPlan(mealPlan: MealPlanEntity)

    @Delete
    suspend fun deleteMealPlan(mealPlan: MealPlanEntity)
}