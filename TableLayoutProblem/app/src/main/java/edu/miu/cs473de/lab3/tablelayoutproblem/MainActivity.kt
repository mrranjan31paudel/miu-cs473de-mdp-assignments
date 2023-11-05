package edu.miu.cs473de.lab3.tablelayoutproblem

import android.os.Bundle
import android.view.View
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.setPadding
import edu.miu.cs473de.lab3.tablelayoutproblem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = viewBinding.root
        setContentView(view)
    }

    private fun getNewTextViewForThisContext(text: String): TextView {
        val newTextView = TextView(this)
        val paddingInDp = 4
        newTextView.text = text
        newTextView.setPadding((resources.displayMetrics.density * paddingInDp + 0.5f).toInt())
        newTextView.setTextColor(resources.getColor(R.color.black))
        newTextView.setBackgroundResource(R.drawable.bordered_cell_back_1)

        return newTextView
    }

    fun onAddButtonClick(view: View) {
        val androidVersion = viewBinding.androidVersion.text?.toString()
        val androidCodeName = viewBinding.androidCodeName.text?.toString()

        if (androidVersion.isNullOrEmpty()) {
            Toast.makeText(this, "Android Version is required!", Toast.LENGTH_SHORT).show()
            return
        }

        if (androidCodeName.isNullOrEmpty()) {
            Toast.makeText(this, "Android Code Name is required!", Toast.LENGTH_SHORT).show()
            return
        }

        val newTableRow = TableRow(this)
        val newVersionTextView = getNewTextViewForThisContext(androidVersion)
        val newCodeNameTextView = getNewTextViewForThisContext(androidCodeName)
        newTableRow.addView(newVersionTextView)
        newTableRow.addView(newCodeNameTextView)
        viewBinding.root.addView(newTableRow)

        viewBinding.androidVersion.text?.clear()
        viewBinding.androidCodeName.text?.clear()
    }
}