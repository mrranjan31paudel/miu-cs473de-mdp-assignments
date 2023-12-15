package edu.miu.cs473de.lab6.foodiepal

import android.view.View
import edu.miu.cs473de.lab6.foodiepal.data.mealplan.MealPlan

interface OnMealPlanItemClickListener {
    fun onMealPlanClick(view: View, mealPlan: MealPlan, position: Int)
}