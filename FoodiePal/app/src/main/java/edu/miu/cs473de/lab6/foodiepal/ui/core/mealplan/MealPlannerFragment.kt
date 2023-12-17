package edu.miu.cs473de.lab6.foodiepal.ui.core.mealplan

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.miu.cs473de.lab6.foodiepal.recyclerviewadapters.MealPlanRecyclerViewAdapter
import edu.miu.cs473de.lab6.foodiepal.listeners.OnMealPlanItemClickListener
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentMealPlannerBinding
import edu.miu.cs473de.lab6.foodiepal.data.mealplan.MealPlan
import edu.miu.cs473de.lab6.foodiepal.data.mealplan.MealPlanEntity
import edu.miu.cs473de.lab6.foodiepal.service.mealplan.MealPlanService
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters

class MealPlannerFragment : Fragment() {

    private lateinit var viewBinding: FragmentMealPlannerBinding
    private lateinit var today: LocalDate
    private lateinit var currDate: LocalDate
    private lateinit var mealPlannerRecyclerView: RecyclerView
    private lateinit var mealPlans: ArrayList<MealPlan>
    private lateinit var plansMap: HashMap<String, MealPlanEntity>
    private var plannerId: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_meal_planner, container, false)

        today = LocalDate.now()
        currDate = today
        plannerId = getLoggedInUserId()

        viewBinding = FragmentMealPlannerBinding.bind(view)
        viewBinding.monthTextView.text = "${today.month.name} ${today.year}"
        viewBinding.prevMonthButton.setOnClickListener {goToPrevMonth()}
        viewBinding.nextMonthButton.setOnClickListener {goToNextMonth()}

        mealPlannerRecyclerView = view.findViewById<RecyclerView>(R.id.meal_planner_recycler_view)
        mealPlannerRecyclerView.layoutManager = LinearLayoutManager(activity)
        mealPlans = ArrayList()
        plansMap = HashMap()
        mealPlannerRecyclerView.adapter = MealPlanRecyclerViewAdapter(generateListOfMealPlanDays(currDate), object:
            OnMealPlanItemClickListener {
            override fun onMealPlanClick(view: View, mealPlan: MealPlan, position: Int) {
                openMealPlanDialog(mealPlan, position)
            }
        })

        return view
    }

    private fun getLoggedInUserId(): Int {
        val sharedPreferences = activity?.getSharedPreferences("app_pref", Context.MODE_PRIVATE) ?: return 0

        return sharedPreferences.getInt(getString(R.string.logged_in_user_id), 0)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getLocalDateFromEpoch(epochSecond: Long): LocalDate {
        val zoneId = ZoneId.systemDefault()
        return Instant.ofEpochSecond(epochSecond).atZone(zoneId).toLocalDate()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMealPlanMapByDateForAYearAndAMonth(year: Int, month: Int): HashMap<String, MealPlanEntity> {
        val map = HashMap<String, MealPlanEntity>()
        try {
            val mealPlans = MealPlanService.getMealPlansByPlannerIdAndMonthAndYear(plannerId, month, year)
            for (plan in mealPlans) {
                val planDate = getLocalDateFromEpoch(plan.date)
                val key = "${planDate.year}-${planDate.month.value}-${planDate.dayOfMonth}"
                map[key] = plan
            }
        }
        catch (e: Exception) {
            Snackbar.make(viewBinding.root, "Failed to load your meal plans!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
        return map
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun generateListOfMealPlanDays(date: LocalDate): ArrayList<MealPlan> {
        mealPlans.clear()
        plansMap.clear()

        plansMap = getMealPlanMapByDateForAYearAndAMonth(date.year, date.month.value)
        // while generating the list, if for a date plan exists, use that plan instead

        var iDate = date.with(TemporalAdjusters.firstDayOfMonth())
        val lastDayOfMonth = date.month.length(date.isLeapYear)
        val blackColor = resources.getColor(R.color.black)
        val greyColor = resources.getColor(R.color.grey_2)
        val secondaryBlueColor = resources.getColor(R.color.blue_2)
        val blueColor = resources.getColor(R.color.blue_1)
        val whiteColor = resources.getColor(R.color.white)

        for ( day in 1..lastDayOfMonth) {
            val isPast = iDate < today
            val key = "${iDate.year}-${iDate.month.value}-${iDate.dayOfMonth}"
            val color = if (isPast) greyColor else {
                if (plansMap[key] != null) whiteColor else blackColor
            }
            val backColor = if (plansMap[key] == null) whiteColor else {
                if (isPast) secondaryBlueColor else blueColor
            }

            mealPlans.add(MealPlan(iDate, plansMap[key]?.plan ?: "", plannerId, color, backColor, !isPast, iDate.dayOfWeek.value == 7))

            if (day < lastDayOfMonth) {
                iDate = iDate.plusDays(1L)
            }
        }

        return mealPlans
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun goToPrevMonth() {
        if (today.month.value - currDate.month.value >= 6) return

        currDate = currDate.minusMonths(1L)
        generateListOfMealPlanDays(currDate)
        viewBinding.monthTextView.text = "${currDate.month.name} ${currDate.year}"
        mealPlannerRecyclerView.adapter?.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun goToNextMonth() {
        if (today.month.value == currDate.month.value) return

        currDate = currDate.plusMonths(1L)
        generateListOfMealPlanDays(currDate)
        viewBinding.monthTextView.text = "${currDate.month.name} ${currDate.year}"
        mealPlannerRecyclerView.adapter?.notifyDataSetChanged()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveOnItemInRecyclerView(mealPlan: MealPlan, mealPlanEntity: MealPlanEntity, position: Int) {
        val key = "${mealPlan.date.year}-${mealPlan.date.month.value}-${mealPlan.date.dayOfMonth}"

        if (plansMap[key] == null) {
            val blueColor = resources.getColor(R.color.blue_1)
            val whiteColor = resources.getColor(R.color.white)
            mealPlan.textColor = whiteColor
            mealPlan.backColor = blueColor
        }
        plansMap[key] = mealPlanEntity
        mealPlannerRecyclerView.adapter?.notifyItemChanged(position)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteOneItemInRecycleView(mealPlan: MealPlan, position: Int) {
        val key = "${mealPlan.date.year}-${mealPlan.date.month.value}-${mealPlan.date.dayOfMonth}"
        plansMap.remove(key)
        val blackColor = resources.getColor(R.color.black)
        val whiteColor = resources.getColor(R.color.white)
        mealPlan.textColor = blackColor
        mealPlan.backColor = whiteColor
        mealPlannerRecyclerView.adapter?.notifyItemChanged(position)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveMealPlan(mealPlan: MealPlan, planString: String, isNew: Boolean, position: Int) {
        try {
            val copyOfMealPlan = mealPlan.clone()
            copyOfMealPlan.plan = planString
            println("ERROR: $plannerId")
            println("ERROR: $copyOfMealPlan")
            val mealPlanEntity = if (isNew) {
                MealPlanService.createMealPlan(copyOfMealPlan)
            }
            else {
                MealPlanService.updateMealPlan(copyOfMealPlan)
            }

            Snackbar.make(viewBinding.root, "Plan saved successfully!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.green_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()

            mealPlan.plan = planString
            saveOnItemInRecyclerView(mealPlan, mealPlanEntity, position)
        }
        catch (e: Exception) {
            println("ERROR:" + e.stackTraceToString())
            Snackbar.make(viewBinding.root, "Failed to save the plan!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteMealPlan(mealPlan: MealPlan, position: Int) {
        try {
            val copyOfMealPlan = mealPlan.clone()
            copyOfMealPlan.plan = ""
            MealPlanService.deleteMealPlan(copyOfMealPlan)
            Snackbar.make(viewBinding.root, "Plan deleted successfully!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.green_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()

            mealPlan.plan = ""
            deleteOneItemInRecycleView(mealPlan, position)
        }
        catch (e: Exception) {
            Snackbar.make(viewBinding.root, "Failed to delete the plan!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun openMealPlanDialog(mealPlan: MealPlan, position: Int) {
        val fragmentManager = activity?.supportFragmentManager ?: return

        val preFragment = fragmentManager.findFragmentByTag(MealPlanDialogFragment.TAG)
        if (preFragment != null) return

        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        val dateString = "${mealPlan.date.month.name} ${mealPlan.date.dayOfMonth}, ${mealPlan.date.year}"
        val newRecipeDialogFragment = MealPlanDialogFragment.newInstance(dateString, mealPlan.plan ?: "")
        newRecipeDialogFragment.show(transaction, MealPlanDialogFragment.TAG)

        fragmentManager.setFragmentResultListener("on_save", viewLifecycleOwner) {requestKey, bundle ->
            val planString = bundle.getString("planString")

            if (mealPlan.plan.isNullOrEmpty()) {
                saveMealPlan(mealPlan, planString ?: "", true, position)
            }
            else {
                saveMealPlan(mealPlan, planString ?: "", false, position)
            }
        }
        fragmentManager.setFragmentResultListener("on_delete", viewLifecycleOwner) {requestKey, bundle ->
            deleteMealPlan(mealPlan, position)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MealPlannerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            MealPlannerFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}