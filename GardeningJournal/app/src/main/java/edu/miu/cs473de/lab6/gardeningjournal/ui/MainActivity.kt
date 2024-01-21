package edu.miu.cs473de.lab6.gardeningjournal.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import edu.miu.cs473de.lab6.gardeningjournal.R

class MainActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.appToolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragmentContainerView) as NavHostFragment
        navigationController = navHostFragment.navController
        NavigationUI.setupActionBarWithNavController(this, navigationController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigationController.navigateUp()
    }
}