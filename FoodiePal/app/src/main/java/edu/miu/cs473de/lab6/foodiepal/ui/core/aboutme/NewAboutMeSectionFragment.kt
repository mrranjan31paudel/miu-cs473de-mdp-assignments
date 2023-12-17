package edu.miu.cs473de.lab6.foodiepal.ui.core.aboutme

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentNewAboutMeSectionBinding
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import edu.miu.cs473de.lab6.foodiepal.service.abouteme.AboutMeService


/**
 * A simple [Fragment] subclass.
 * Use the [NewAboutMeSectionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewAboutMeSectionFragment : DialogFragment() {

    private lateinit var viewBinding: FragmentNewAboutMeSectionBinding

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding = FragmentNewAboutMeSectionBinding.inflate(LayoutInflater.from(context))
        viewBinding.addSectionButton.setOnClickListener { addAboutMeItem() }
        viewBinding.cancelButton.setOnClickListener { dismiss() }
    }

    override fun getTheme(): Int {
        return R.style.Theme_FoodiePal
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_about_me_section, container, false)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addAboutMeItem() {
        val fieldMap = HashMap<String, TextInputLayout?>()
        fieldMap["aboutMeItemTitle"] = viewBinding.aboutMeItemTitle
        fieldMap["aboutMeItemDescription"] = viewBinding.aboutMeItemDescription

        viewBinding.addSectionButton.isEnabled = false
        viewBinding.cancelButton.isEnabled = false

        try {
            val newAboutMeItemId = AboutMeService.createAboutMeItem(
                fieldMap["aboutMeItemTitle"]?.editText?.text.toString(),
                fieldMap["aboutMeItemDescription"]?.editText?.text.toString()
            )

            parentFragmentManager.setFragmentResult("on_creation_success", bundleOf("newAboutMeItemId" to newAboutMeItemId))
            dismiss()
        }
        catch (e: ValidationException) {
            fieldMap[e.field]?.editText?.error = e.message
        }
        catch (e: Exception) {
            println("ERRORs: $e")
            val message = "Oops! Something went wrong."
            Snackbar.make(viewBinding.root, message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
        finally {
            viewBinding.addSectionButton.isEnabled = true
            viewBinding.cancelButton.isEnabled = true
        }
    }

    companion object {
        val TAG = "aboutme_form"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            NewAboutMeSectionFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}