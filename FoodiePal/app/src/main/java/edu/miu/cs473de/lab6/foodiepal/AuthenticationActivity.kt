package edu.miu.cs473de.lab6.foodiepal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.miu.cs473de.lab6.foodiepal.service.DatabaseService
import edu.miu.cs473de.lab6.foodiepal.ui.auth.LoginFragment

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        loadFragmentBasedOnUserSession()
        loadDatabase()
        loadDummyDataInRoomStorage()
    }

    private fun loadFragmentBasedOnUserSession() {
        loadLoginFragment()

        // todo: check user session and load fragment or activity as required
    }

    private fun loadLoginFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = LoginFragment.newInstance()
        fragmentTransaction.replace(R.id.auth_fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun loadDatabase() {
        DatabaseService.initDb(applicationContext)
    }

    private fun loadDummyDataInRoomStorage() {

    }
}