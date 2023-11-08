package edu.miu.cs473de.lab3.walmartloginpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import edu.miu.cs473de.lab3.walmartloginpage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val users: ArrayList<User> = ArrayList()

    private fun initializeUsersList() {
        users.add(User("John", "Cena", "johncena", "thisisjohn"))
        users.add(User("Tina", "Rodriguez", "tinarodri", "thisistina"))
        users.add(User("John", "Smith", "johnsmith", "thisisjohn"))
        users.add(User("Gina", "Kinch", "ginakinch", "thisisgina"))
        users.add(User("Tom", "Hank", "tomhank", "thisistom"))
    }

    private lateinit var viewbinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUsersList()
        viewbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewbinding.root)
    }

    fun onSignIn(view: View) {
        val username = viewbinding.email.text
        val password = viewbinding.password.text

        val foundUser = users.find { user -> user.username == username.toString() && user.password == password.toString() }

        if (foundUser == null) {
            Toast.makeText(this, "Error: Email and password do not match!", Toast.LENGTH_SHORT).show()
            return
        }

        // go to category page
        val shoppingIntent = Intent(this, ShoppingActivity::class.java)
        shoppingIntent.putExtra("user", foundUser)
        startActivity(shoppingIntent)
    }
}