package edu.miu.cs473de.lab6.foodiepal.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.data.aboutme.AboutMeItem
import edu.miu.cs473de.lab6.foodiepal.service.abouteme.AboutMeService
import edu.miu.cs473de.lab6.foodiepal.service.DatabaseService
import edu.miu.cs473de.lab6.foodiepal.service.user.UserService
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

    private fun loadOwnerData() {
        try {
            val ownerEmail = getString(R.string.owner_email)
            val owner = UserService.getUserByEmail(ownerEmail)
            if (owner != null) return
            UserService.createUser("Ranjan", "Paudel", ownerEmail, "password", "password")
        }
        catch (e: Exception) {
            println("ERRORs: Failed to add the owner! $e")
        }
    }

    private fun loadAboutMeData() {
        try {
            val aboutMeItems = AboutMeService.getAllItems()
            if (aboutMeItems.size == 0) {
                val cjt = getString(R.string.culinary_journey_title)
                val cjd = getString(R.string.culinary_journey_desc)
                val frt = getString(R.string.favorite_recipes_title)
                val frd = getString(R.string.favorite_recipes_desc)
                val fpt = getString(R.string.food_philosophy_title)
                val fpd = getString(R.string.food_philosophy_desc)

                val items = arrayListOf(
                    AboutMeItem(0, cjt, cjd),
                    AboutMeItem(0, frt, frd),
                    AboutMeItem(0, fpt, fpd)
                )
                AboutMeService.insertBulkItems(items)
            }
        }
        catch (e: Exception) {
            println("ERRORs: Failed to load about me items! $e")
        }
    }

    private fun loadDummyDataInRoomStorage() {
        println("Hello f")
        // add a user as the owner if not exist already.
        loadOwnerData()
        println("Hello f")
        // add about me items if not existing already
        loadAboutMeData()
    }

    private fun checkUserSession() {

    }
}
