package edu.miu.cs473de.lab6.foodiepal.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentRegisterBinding
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import edu.miu.cs473de.lab6.foodiepal.service.UserService

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {

    private var registerViewBinding: FragmentRegisterBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val loginButton = view.findViewById<Button>(R.id.go_to_login_button)
        val createButton = view.findViewById<Button>(R.id.create_button)
        loginButton.setOnClickListener { v -> onLoginButtonClick(v) }
        createButton.setOnClickListener { v -> onCreateAccount(v) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerViewBinding = FragmentRegisterBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        registerViewBinding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RegisterFragmet.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            RegisterFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun gotToLogin() {
        val fragmentManager = activity?.supportFragmentManager

        if (fragmentManager != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = LoginFragment.newInstance()
            fragmentTransaction.replace(R.id.auth_fragment_container, fragment)
            fragmentTransaction.commit()
        }
    }

    private fun onLoginButtonClick(view: View) {
        gotToLogin()
    }

    private fun onCreateAccount(view: View) {
        val fieldMap = HashMap<String, TextInputLayout?>()
        fieldMap["firstName"] = registerViewBinding?.editTextFirstName
        fieldMap["lastName"] = registerViewBinding?.editTextLastName
        fieldMap["email"] = registerViewBinding?.editTextTextEmailAddress
        fieldMap["password"] = registerViewBinding?.editTextTextPassword
        fieldMap["confirmPassword"] = registerViewBinding?.editTextConfirmPassword

        try {
            UserService.createUser(
                fieldMap["firstName"]?.editText?.text.toString(),
                fieldMap["lastName"]?.editText?.text.toString(),
                fieldMap["email"]?.editText?.text.toString(),
                fieldMap["password"]?.editText?.text.toString(),
                fieldMap["confirmPassword"]?.editText?.text.toString())

            for (inputLayout: TextInputLayout? in fieldMap.values) {
                inputLayout?.editText?.text?.clear()
            }
            Snackbar.make(view, "Account created successfully!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.green_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
            gotToLogin()
        }
        catch (e: ValidationException) {
            fieldMap[e.field]?.editText?.error = e.message
        }
        catch (e: Exception) {
            var message = "Oops! Something went wrong."

            if (e.message?.matches(Regex("^UNIQUE.*")) == true) {
                message = "Account with email already exists!"
            }
            Snackbar.make(view,  message, Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
    }
}