package edu.miu.cs473de.lab3.walmartloginpage

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import edu.miu.cs473de.lab3.walmartloginpage.databinding.ActivityShoppingBinding

class ShoppingActivity() : AppCompatActivity() {

    private lateinit var viewbinding: ActivityShoppingBinding

    private var user: User? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewbinding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(viewbinding.root)

        user = intent.getSerializableExtra("user", User::class.java)

        showWelcomeText()
    }

    private fun showWelcomeText() {
        viewbinding.welcomeText.text = getString(R.string.welcome_message, user?.username?:"user")
    }

    fun onCategoryClick(view: View) {
        println("here---------------->")
        val categoryName: String = when(view.id) {
            R.id.electronics_card -> "Electronics"
            R.id.clothing_card -> "Clothing"
            R.id.beauty_card -> "Beauty"
            R.id.food_card -> "Food"
            else -> ""
        }
        Toast.makeText(this, "You have chosen the $categoryName category of shopping", Toast.LENGTH_SHORT).show()
    }

}