package edu.miu.cs473de.lab6.foodiepal.ui.core

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentNewRecipeBinding
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import edu.miu.cs473de.lab6.foodiepal.service.RecipeService

class NewRecipeDialogFragment : DialogFragment() {

    private lateinit var viewBinding: FragmentNewRecipeBinding

    override fun getTheme(): Int {
        return R.style.Theme_FoodiePal
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = FragmentNewRecipeBinding.inflate(LayoutInflater.from(context))

        viewBinding.createNewRecipeButton.setOnClickListener{ addNewRecipe() }
        viewBinding.cancelButton.setOnClickListener { dismiss() }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = MaterialAlertDialogBuilder(this.requireContext(), com.google.android.material.R.style.MaterialAlertDialog_Material3)
        builder
            .setView(viewBinding.root)
            .setBackgroundInsetStart(0)
            .setBackgroundInsetEnd(0)
            .setBackgroundInsetTop(0)
            .setBackgroundInsetBottom(0)

        return builder.create()
    }

    private fun getLoggedInUserId(): Int {
        val sharedPreferences = activity?.getSharedPreferences("app_pref", Context.MODE_PRIVATE) ?: return 0

        return sharedPreferences.getInt(getString(R.string.logged_in_user_id), 0)
    }

    private fun addNewRecipe() {
        val fieldMap = HashMap<String, TextInputLayout?>()
        fieldMap["newRecipeName"] = viewBinding.newRecipeName
        fieldMap["newRecipeCookingTime"] = viewBinding.newRecipeCookingTime
        fieldMap["newRecipeRating"] = viewBinding.newRecipeRating
        fieldMap["newRecipeDescription"] = viewBinding.newRecipeDescription

        viewBinding.createNewRecipeButton.isEnabled = false
        viewBinding.cancelButton.isEnabled = false

        try {
            val newRecipeId = RecipeService.createRecipe(
                fieldMap["newRecipeName"]?.editText?.text.toString(),
                fieldMap["newRecipeCookingTime"]?.editText?.text.toString(),
                fieldMap["newRecipeRating"]?.editText?.text.toString(),
                fieldMap["newRecipeDescription"]?.editText?.text.toString(),
                getLoggedInUserId())
            for (inputLayout: TextInputLayout? in fieldMap.values) {
                inputLayout?.editText?.text?.clear()
            }
            Snackbar.make(viewBinding.root, "Account created successfully!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.green_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
            parentFragmentManager.setFragmentResult("on_creation_success", bundleOf("newRecipeId" to newRecipeId))
            dismiss()
        }
        catch (e: ValidationException) {
            fieldMap[e.field]?.editText?.error = e.message
        }
        catch (e: Exception) {
            val message = "Oops! Something went wrong."
            Snackbar.make(viewBinding.root, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
        finally {
            viewBinding.createNewRecipeButton.isEnabled = true
            viewBinding.cancelButton.isEnabled = true
        }
    }

    companion object {
        val TAG: String? = "new_recipe_fragment"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NewRecipeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            NewRecipeDialogFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}