package edu.miu.cs473de.lab2.dinnerdecider

import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import edu.miu.cs473de.lab2.dinnerdecider.databinding.ActivityMainBinding
import java.lang.RuntimeException
import kotlin.math.nextDown

class MainActivity : ComponentActivity() {
    private val foodList = ArrayList(listOf("Hamburger", "Pizza", "Mexican", "American", "Chinese"))

    // Using this recommended way to bind the view as the "kotlin-android-extensions" plugin is deprecated
    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
    }

    /**
     * This function generates random value ranging from '`0`' to '`upperBound - 1`'
     */
    private fun getRandomPositiveInt(upperBound: Int): Int {
        val random = upperBound * (Math.random()/ 1.0.nextDown())

        return random.toInt()
    }

    /**
     * Click event handler for '`DECIDE!`' button
     */
    fun onDecideClick(view: View) {
        try {
            val randomIndex = getRandomPositiveInt(foodList.size)
            viewBinding.decidedFoodName.text = foodList[randomIndex]
        } catch (error: RuntimeException) {
            viewBinding.decidedFoodName.text = "Failed to decide!"
        }
    }

    /**
     * Click event handler for '`ADD FOOD`' button
     */
    fun onAddFoodClick(view: View) {
        val inputFieldValue = viewBinding.newFoodInput.text

        if (!inputFieldValue.isNullOrEmpty()) {
            foodList.add(inputFieldValue.toString())
            viewBinding.newFoodInput.text.clear()
        }
    }
}
