package edu.miu.cs473de.lab6.foodiepal.data.mealplan

import java.time.LocalDate

data class MealPlan(var date: LocalDate, var plan: String?, var plannerId: Int) {
    var textColor: Int = 0
    var backColor: Int = 0
    var isEnabled: Boolean = true
    var isLastDayOfWeek: Boolean = false

    constructor(date: LocalDate, plan: String?, plannerId: Int, textColor: Int, backColor: Int, isEnabled: Boolean, isLastDayOfWeek: Boolean) : this(date, plan, plannerId) {
        this.textColor = textColor
        this.backColor = backColor
        this.isEnabled = isEnabled
        this.isLastDayOfWeek = isLastDayOfWeek
    }

    fun clone(): MealPlan {
        return MealPlan(date, plan, plannerId, textColor, backColor, isEnabled, isLastDayOfWeek)
    }
}
