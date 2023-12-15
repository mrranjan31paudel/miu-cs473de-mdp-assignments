package edu.miu.cs473de.lab6.foodiepal

import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.divider.MaterialDivider
import edu.miu.cs473de.lab6.foodiepal.data.mealplan.MealPlan

class MealPlanRecyclerViewAdapter(var mealPlans: ArrayList<MealPlan>, var clickListener: OnMealPlanItemClickListener): RecyclerView.Adapter<MealPlanRecyclerViewAdapter.MealPlanViewHolder>() {

    private var context: Context? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MealPlanRecyclerViewAdapter.MealPlanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.meal_plan_item, parent, false)
        context = parent.context
        return MealPlanViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(
        holder: MealPlanRecyclerViewAdapter.MealPlanViewHolder,
        position: Int
    ) {
        holder.dayOfWeek.text = mealPlans[position].date.dayOfWeek.name.subSequence(0, 3)
        holder.dayOfMonth.text = mealPlans[position].date.dayOfMonth.toString()
        holder.mealPlanTextView.text = mealPlans[position].plan ?: ""
        holder.dayOfWeek.setTextColor(mealPlans[position].textColor)
        holder.dayOfMonth.setTextColor(mealPlans[position].textColor)
        holder.mealPlanTextView.setTextColor(mealPlans[position].textColor)
        holder.mealPlanContainer.setBackgroundColor(mealPlans[position].backColor)
        holder.mealPlanContainer.isClickable = mealPlans[position].isEnabled
        holder.mealPlanContainer.isFocusable = mealPlans[position].isEnabled
        holder.mealPlanContainer.isEnabled = mealPlans[position].isEnabled
        holder.weekDivider.isVisible = mealPlans[position].isLastDayOfWeek
        holder.mealPlanContainer.setOnClickListener{v ->
            if (!mealPlans[position].isEnabled) return@setOnClickListener
            clickListener.onMealPlanClick(v, mealPlans[position], position)
        }
    }

    override fun getItemCount(): Int {
        return mealPlans.size
    }

    inner class MealPlanViewHolder(mealPlanView: View): RecyclerView.ViewHolder(mealPlanView) {
        var dayOfWeek: TextView = mealPlanView.findViewById(R.id.dayOfWeek)
        var dayOfMonth: TextView = mealPlanView.findViewById(R.id.dayOfMonth)
        var mealPlanTextView: TextView = mealPlanView.findViewById(R.id.mealPlan)
        var mealPlanContainer: MaterialCardView = mealPlanView.findViewById(R.id.mealPlanContainer)
        var weekDivider: MaterialDivider = mealPlanView.findViewById(R.id.weekDivider)
    }
}