package edu.miu.cs473de.lab6.foodiepal

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import edu.miu.cs473de.lab6.foodiepal.ui.core.AboutMeFragment
import edu.miu.cs473de.lab6.foodiepal.ui.core.BlogFragment
import edu.miu.cs473de.lab6.foodiepal.ui.core.ContactFragment
import edu.miu.cs473de.lab6.foodiepal.ui.core.MealPlannerFragment
import edu.miu.cs473de.lab6.foodiepal.ui.core.RecipeFragment

class CoreFragmentAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> RecipeFragment()
            1 -> MealPlannerFragment()
            2 -> BlogFragment()
            3 -> ContactFragment()
            4 -> AboutMeFragment()
            else -> Fragment()
        }
    }
}