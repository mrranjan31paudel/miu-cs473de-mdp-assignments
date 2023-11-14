package edu.miu.cs473de.lab5.quizapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import edu.miu.cs473de.lab5.quizapp.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    private var isSubmitted = false
    private var questionOneAnswer = ""
    private val questionOneExpectedAnswer = "println(\"Hello World!\")"

    private var questionTwoAnswer = mutableMapOf<String, Int>()
    private var questionTwoExpectedAnswer = mapOf<String, Int>("\"Hello \$name\"" to 1, "\"Hello \${name}\"" to 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.question1RadioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (!isSubmitted) {
                val checkedButton = viewBinding.root.findViewById<RadioButton>(checkedId)
                questionOneAnswer = (checkedButton?.text?:"").toString()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun onSubmit(view: View) {
        if (isSubmitted) {
            Toast.makeText(this, "Reset the previous submission first!", Toast.LENGTH_SHORT).show()
            return
        }

        if (questionOneAnswer.isEmpty()) {
            Toast.makeText(this, "Question No. 1 not answered!", Toast.LENGTH_SHORT).show()
            return
        }

        if (questionTwoAnswer.isEmpty()) {
            Toast.makeText(this, "Question No. 2 not answered!", Toast.LENGTH_SHORT).show()
            return
        }

        var score = 100
        var resultPrefix = "Congratulations"
        if (questionOneAnswer != questionOneExpectedAnswer) {
            score -= 50
        }
        if (questionTwoAnswer.size != 2 || questionTwoAnswer.keys.sortedDescending().joinToString(",") != questionTwoExpectedAnswer.keys.sortedDescending().joinToString(",")) {
            score -= 50
            resultPrefix = "Sorry"
        }

        isSubmitted = true
        val dialogBuilder = AlertDialog.Builder(this)
        val firstApiFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val date = LocalDateTime.now().format(firstApiFormat)
        dialogBuilder.setTitle("Result")
        dialogBuilder.setMessage("$resultPrefix! You have submitted at $date, you achieved $score%")
        dialogBuilder.setPositiveButton("OK") {dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog = dialogBuilder.create()
        alertDialog.show()
    }

    fun onQuestionTwoOptionSelect(view: View) {
        if (isSubmitted) {
            return
        }
        val checkBox = view as CheckBox
        println("Hello : ${checkBox.text} ${checkBox.isSelected}")
        if (!checkBox.isSelected) {
            checkBox.isSelected = true
            questionTwoAnswer[checkBox.text.toString()] = 1
        }
        else {
            checkBox.isSelected = false
            questionTwoAnswer.remove(checkBox.text.toString())
        }
    }

    fun onReset(view: View) {
        if (isSubmitted) {
            viewBinding.question1RadioGroup.clearCheck()
            viewBinding.question2Option1.isChecked = false
            viewBinding.question2Option2.isChecked = false
            viewBinding.question2Option3.isChecked = false
            viewBinding.question2Option4.isChecked = false
            viewBinding.question2Option1.isSelected = false
            viewBinding.question2Option2.isSelected = false
            viewBinding.question2Option3.isSelected = false
            viewBinding.question2Option4.isSelected = false

            questionOneAnswer = ""
            questionTwoAnswer.entries.clear()
            isSubmitted = false
        }
    }
}