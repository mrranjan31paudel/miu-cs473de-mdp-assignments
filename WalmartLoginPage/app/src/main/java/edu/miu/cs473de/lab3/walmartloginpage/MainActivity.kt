package edu.miu.cs473de.lab3.walmartloginpage

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import edu.miu.cs473de.lab3.walmartloginpage.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val users: ArrayList<User> = ArrayList()

    private fun initializeUsersList() {
        users.add(User("John", "Cena", "johncena@jptmail.com", "thisisjohn"))
        users.add(User("Tina", "Rodriguez", "tinarodri@jptmail.com", "thisistina"))
        users.add(User("John", "Smith", "johnsmith@jptmail.com", "thisisjohn"))
        users.add(User("Gina", "Kinch", "ginakinch@jptmail.com", "thisisgina"))
        users.add(User("Tom", "Hank", "tomhank@jptmail.com", "thisistom"))
    }

    private lateinit var viewbinding: ActivityMainBinding

    private lateinit var registerActivityLauncher: ActivityResultLauncher<Intent>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeUsersList()
        viewbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewbinding.root)

        registerActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val newUser = result.data
                    ?.getSerializableExtra("new-user", User::class.java)
                if (newUser != null) {
                    addUser(newUser)
                }
                else {
                    Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show()
            }
        }
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
        username.clear()
        password.clear()
        startActivity(shoppingIntent)
    }

    private fun addUser(user: User) {
        users.add(user)
        Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
    }

    fun onCreateNewAccount(view: View) {
        val registerIntent = Intent(this, RegisterActivity::class.java)
        registerActivityLauncher.launch(registerIntent)
    }

    fun onForgotPassword(view: View) {
        val email = viewbinding.email.text

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }

        val password = users.find { user -> user.username == email.toString() }?.password

        if (password == null) {
            Toast.makeText(this, "Account does not exist!", Toast.LENGTH_SHORT).show()
            return
        }

        val emailIntent = Intent()
        emailIntent.action = Intent.ACTION_SEND
        emailIntent.type = "text/plain"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email.toString())
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Forgotten Password")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Your password is '$password'")

        startActivity(emailIntent)
    }
}