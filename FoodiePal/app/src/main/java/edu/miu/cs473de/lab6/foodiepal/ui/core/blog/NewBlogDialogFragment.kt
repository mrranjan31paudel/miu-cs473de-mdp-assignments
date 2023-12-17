package edu.miu.cs473de.lab6.foodiepal.ui.core.blog

import android.app.Dialog
import android.content.Context
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
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentNewBlogDialogBinding
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import edu.miu.cs473de.lab6.foodiepal.service.blog.BlogService


/**
 * A simple [Fragment] subclass.
 * Use the [NewBlogDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewBlogDialogFragment : DialogFragment() {

    private lateinit var viewBinding: FragmentNewBlogDialogBinding
    private var loggedInUserId = 0

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loggedInUserId = getLoggedInUserId()
        viewBinding = FragmentNewBlogDialogBinding.inflate(LayoutInflater.from(context))
        viewBinding.postBlogButton.setOnClickListener { postNewBlog() }
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
        return inflater.inflate(R.layout.fragment_new_blog_dialog, container, false)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun postNewBlog() {
        val fieldMap = HashMap<String, TextInputLayout?>()
        fieldMap["blogTitle"] = viewBinding.blogTitle
        fieldMap["blogContent"] = viewBinding.blogContent

        viewBinding.postBlogButton.isEnabled = false
        viewBinding.cancelButton.isEnabled = false

        try {
            val newBlogId = BlogService.postNewBlog(
                fieldMap["blogTitle"]?.editText?.text.toString(),
                fieldMap["blogContent"]?.editText?.text.toString(),
                loggedInUserId
            )

            parentFragmentManager.setFragmentResult("on_creation_success", bundleOf("newBlogId" to newBlogId))
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
            viewBinding.postBlogButton.isEnabled = true
            viewBinding.cancelButton.isEnabled = true
        }
    }

    companion object {
        val TAG = "blog_form"
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NewBlogDialogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            NewBlogDialogFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}