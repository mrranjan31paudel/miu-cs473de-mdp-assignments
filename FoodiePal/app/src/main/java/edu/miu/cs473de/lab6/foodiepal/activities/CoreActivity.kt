package edu.miu.cs473de.lab6.foodiepal.activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.databinding.ActivityCoreBinding
import edu.miu.cs473de.lab6.foodiepal.recyclerviewadapters.CoreFragmentAdapter

class CoreActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityCoreBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityCoreBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        val viewPageAdapter = CoreFragmentAdapter(this)

        viewBinding.appToolbar.title = getString(R.string.recipes_label)
        viewBinding.appViewPager.adapter = viewPageAdapter
        viewBinding.appTabLayout.tabGravity = TabLayout.GRAVITY_FILL
        setSupportActionBar(viewBinding.appToolbar)

        TabLayoutMediator(viewBinding.appTabLayout, viewBinding.appViewPager) { tab, position ->
            when(position) {
                0 -> {
                    tab.text = getString(R.string.recipes_label)
                }
                1 -> {
                    tab.text = getString(R.string.meal_planner_label)
                }
                2 -> {
                    tab.text = getString(R.string.blog_label)
                }
                3 -> {
                    tab.text = getString(R.string.contact_label)
                }
                4 -> {
                    tab.text = getString(R.string.about_me_label)
                }
            }
        }.attach()

        viewBinding.appTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewBinding.appToolbar.title = tab?.text ?: "FoodiePal"
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        viewBinding.appBottomNavigation.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.bot_nav_recipes -> viewBinding.appViewPager.currentItem = 0
                R.id.bot_nav_meal_planner -> viewBinding.appViewPager.currentItem = 1
                R.id.bot_nav_blog -> viewBinding.appViewPager.currentItem = 2
            }

            true
        }

    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_options, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout_option -> {
                logout()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun removeUserSession(sharedPreferences: SharedPreferences) {
        with (sharedPreferences.edit()) {
            remove(getString(R.string.logged_in_user_id))
            remove(getString(R.string.logged_in_user_email))
            remove(getString(R.string.logged_in_user_name))
            commit()
        }
    }

    private fun logout() {
        val sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE)

        if (sharedPreferences != null) {
            removeUserSession(sharedPreferences)
        }

        finish()
    }

    private fun onBack() {
        val dialogBuilder = MaterialAlertDialogBuilder(this)
        dialogBuilder
            .setTitle("Exit")
            .setMessage("Are you sure to exit?")
            .setPositiveButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .setNegativeButton("Yes"){ dialog, _ ->
                dialog.dismiss()
                finish()
            }
        dialogBuilder.create().show()
    }
}