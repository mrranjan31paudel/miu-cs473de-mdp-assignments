package edu.miu.cs473de.lab6.foodiepal

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.miu.cs473de.lab6.foodiepal.service.DatabaseService
import edu.miu.cs473de.lab6.foodiepal.ui.auth.LoginFragment

class AuthenticationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        loadDatabase()
        loadDummyDataInRoomStorage()
    }

    override fun onStart() {
        super.onStart()

        loadFragmentBasedOnUserSession()
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("app_pref", Context.MODE_PRIVATE) ?: return false

        val userid = sharedPreferences.getInt(getString(R.string.logged_in_user_id), 0)
        val email = sharedPreferences.getString(getString(R.string.logged_in_user_email), null)
        val name = sharedPreferences.getString(getString(R.string.logged_in_user_name), null)

        if (userid == 0 || email == null || name == null) {
            return false
        }

        println("Hello: $userid, $email, $name")

        return true
    }

    private fun loadFragmentBasedOnUserSession() {
        if (!isUserLoggedIn()) {
            loadLoginFragment()
            return
        }

        loadCoreActivity()
    }

    private fun loadLoginFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        val fragment = LoginFragment.newInstance()
        fragmentTransaction.replace(R.id.auth_fragment_container, fragment)
        fragmentTransaction.commit()
    }

    private fun loadCoreActivity() {
        val coreIntent = Intent(this, CoreActivity::class.java)
        startActivity(coreIntent)
    }

    private fun loadDatabase() {
        DatabaseService.initDb(applicationContext)
    }

    private fun loadDummyDataInRoomStorage() {

    }

    private fun checkUserSession() {

    }
}
