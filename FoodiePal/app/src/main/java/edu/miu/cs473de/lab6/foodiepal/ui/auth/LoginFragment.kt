package edu.miu.cs473de.lab6.foodiepal.ui.auth

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import edu.miu.cs473de.lab6.foodiepal.activities.CoreActivity
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.data.user.User
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentLoginBinding
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import edu.miu.cs473de.lab6.foodiepal.service.user.UserService

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private var loginViewBinding: FragmentLoginBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val createNewAccountButton = view.findViewById<Button>(R.id.go_to_create_new_account_button)
        val loginButton = view.findViewById<Button>(R.id.login_button)
        createNewAccountButton.setOnClickListener { view -> onCreateNewAccountButtonClick(view) }
        loginButton.setOnClickListener { view -> onLogin(view) }

        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginViewBinding = FragmentLoginBinding.bind(view)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loginViewBinding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment LoginFragment.
         */
        @JvmStatic
        fun newInstance() =
            LoginFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    private fun goToRegister() {
        val fragmentManager = activity?.supportFragmentManager

        if (fragmentManager != null) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            val fragment = RegisterFragment.newInstance()
            fragmentTransaction.replace(R.id.auth_fragment_container, fragment)
            fragmentTransaction.commit()
        }
    }

    private fun goToCoreActivity() {
        val coreActivityIntent = Intent(context, CoreActivity::class.java)
        startActivity(coreActivityIntent)
    }

    private fun setUserSession(user: User, sharedPreferences: SharedPreferences) {

        with (sharedPreferences.edit()) {
            putInt(getString(R.string.logged_in_user_id), user.id)
            putString(getString(R.string.logged_in_user_email), user.email)
            putString(getString(R.string.logged_in_user_name), "${user.firstName} ${user.lastName}")
            apply()
        }
    }

    private fun onCreateNewAccountButtonClick(view: View) {
        goToRegister()
    }

    private fun onLogin(view: View) {
        val fieldMap = HashMap<String, TextInputLayout?>()

        fieldMap["email"] = loginViewBinding?.editTextTextEmailAddress
        fieldMap["password"] = loginViewBinding?.editTextTextPassword
        try {
            val user = UserService.loginUser(
                fieldMap["email"]?.editText?.text.toString(),
                fieldMap["password"]?.editText?.text.toString())

            for (inputLayout: TextInputLayout? in fieldMap.values) {
                inputLayout?.editText?.text?.clear()
            }
            Snackbar.make(view, "Login successful!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.green_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()

            val authenticationActivity = activity
            val sharedPreferences = authenticationActivity?.getSharedPreferences("app_pref", Context.MODE_PRIVATE) ?: return
            setUserSession(user, sharedPreferences)

            goToCoreActivity()
        }
        catch (e: ValidationException) {
            fieldMap[e.field]?.editText?.error = e.message
        }
        catch (e: Exception) {
            Snackbar.make(view, e.message?:"Oops! Something went wrong.", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
        }
    }
}