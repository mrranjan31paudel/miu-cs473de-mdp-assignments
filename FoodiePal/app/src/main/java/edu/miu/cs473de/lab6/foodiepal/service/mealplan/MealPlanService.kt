package edu.miu.cs473de.lab6.foodiepal.service.mealplan

import android.os.Build
import androidx.annotation.RequiresApi
import edu.miu.cs473de.lab6.foodiepal.data.mealplan.MealPlan
import edu.miu.cs473de.lab6.foodiepal.data.mealplan.MealPlanEntity
import edu.miu.cs473de.lab6.foodiepal.service.DatabaseService
import kotlinx.coroutines.runBlocking
import java.time.ZoneId

class MealPlanService {

    companion object {

        fun getMealPlansByPlannerIdAndMonthAndYear(plannerId: Int, month: Int, year: Int): ArrayList<MealPlanEntity> {
            val mealPlanDao = DatabaseService.db?.mealPlanDao()
            val plans = runBlocking {
                return@runBlocking mealPlanDao?.getMealPlansByPlannerIdAndMonthAndYear(plannerId, month, year)
            }

            return plans as ArrayList<MealPlanEntity>
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun createMealPlan(mealPlan: MealPlan): MealPlanEntity {
            if (mealPlan.plan?.isEmpty() == true) {
                throw Exception("Try again with proper plan description!")
            }

            val zoneId = ZoneId.systemDefault()
            val dateInSecond = mealPlan.date.atStartOfDay().atZone(zoneId).toEpochSecond()
            val newMealPlan = MealPlanEntity(dateInSecond, mealPlan.plan ?: "", mealPlan.plannerId)
            val mealPlanDao = DatabaseService.db?.mealPlanDao()
            runBlocking {
                mealPlanDao?.createMealPlan(newMealPlan)
            }
            return newMealPlan
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun updateMealPlan(mealPlan: MealPlan): MealPlanEntity {
            if (mealPlan.plan?.isEmpty() == true) {
                throw Exception("Try again with proper plan description!")
            }
            val zoneId = ZoneId.systemDefault()
            val dateInSecond = mealPlan.date.atStartOfDay().atZone(zoneId).toEpochSecond()
            val updatedPlan = MealPlanEntity(dateInSecond, mealPlan.plan ?: "", mealPlan.plannerId)
            val mealPlanDao = DatabaseService.db?.mealPlanDao()
            runBlocking {
                mealPlanDao?.updateMealPlan(updatedPlan)
            }
            return updatedPlan
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun deleteMealPlan(mealPlan: MealPlan) {
            val zoneId = ZoneId.systemDefault()
            val dateInSecond = mealPlan.date.atStartOfDay().atZone(zoneId).toEpochSecond()
            val planToDelete = MealPlanEntity(dateInSecond, mealPlan.plan ?: "", mealPlan.plannerId)
            val mealPlanDao = DatabaseService.db?.mealPlanDao()
            runBlocking {
                mealPlanDao?.deleteMealPlan(planToDelete)
            }
        }
    }
}