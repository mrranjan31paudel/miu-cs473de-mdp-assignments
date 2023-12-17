package edu.miu.cs473de.lab6.foodiepal.ui.core.mealplan

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentMealPlanDialogBinding

private const val DATE_STRING = "dateString"
private const val MEAL_PLAN = "mealPlanString"

/**
 * A simple [Fragment] subclass.
 * Use the [MealPlanDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MealPlanDialogFragment : DialogFragment() {
    // TODO: Rename and change types of parameters
    private var dateString: String? = null
    private var mealPlanString: String? = null
    private lateinit var viewBinding: FragmentMealPlanDialogBinding

    override fun getTheme(): Int {
        return R.style.Theme_FoodiePal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dateString = it.getString(DATE_STRING)
            mealPlanString = it.getString(MEAL_PLAN)
        }

        viewBinding = FragmentMealPlanDialogBinding.inflate(LayoutInflater.from(context))
        viewBinding.mealPlanDate.text = dateString
        viewBinding.mealPlanDescription.editText?.setText(mealPlanString)

        viewBinding.saveButton.setOnClickListener{ onSave() }
        viewBinding.cancelButton.setOnClickListener { dismiss() }
        viewBinding.deleteButton.setOnClickListener { onDelete() }
    }

    private fun onSave() {
        val planText = viewBinding.mealPlanDescription.editText

        if (planText?.text?.isEmpty() == true || planText?.text.toString().length < 5 || planText?.text.toString().length > 200 ) {
            planText?.error = "Required with at least 5 characters and max 200 characters"
            return
        }

        parentFragmentManager.setFragmentResult("on_save",
            bundleOf("planString" to planText?.text.toString())
        )
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(
            this.requireContext(),
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
        builder
            .setView(viewBinding.root)
            .setBackgroundInsetStart(0)
            .setBackgroundInsetEnd(0)
            .setBackgroundInsetTop(0)
            .setBackgroundInsetBottom(0)

        return builder.create()
    }

    private fun onDelete() {
        parentFragmentManager.setFragmentResult("on_delete", bundleOf("planString" to ""))
        dismiss()
    }

    companion object {
        const val TAG: String = "new_meal_planner_fragment"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param dateString Parameter 1.
         * @param mealPlanString Parameter 2.
         * @return A new instance of fragment MealPlanDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(dateString: String, mealPlanString: String) =
            MealPlanDialogFragment().apply {
                arguments = Bundle().apply {
                    putString(DATE_STRING, dateString)
                    putString(MEAL_PLAN, mealPlanString)
                }
            }
    }
}