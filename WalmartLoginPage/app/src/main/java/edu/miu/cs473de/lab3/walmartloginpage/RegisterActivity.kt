package edu.miu.cs473de.lab3.walmartloginpage

import android.app.Activity
import android.app.Instrumentation.ActivityResult
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import edu.miu.cs473de.lab3.walmartloginpage.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var viewBinding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }



    fun onRegister(view: View) {
        val firstName = viewBinding.registerFirstName.text
        if (firstName.isEmpty()) {
            Toast.makeText(this, "First name is required", Toast.LENGTH_SHORT).show()
            return
        }

        val lastName = viewBinding.registerLastName.text
        if (lastName.isEmpty()) {
            Toast.makeText(this, "Last name is required", Toast.LENGTH_SHORT).show()
            return
        }

        val emailAddress = viewBinding.registerEmail.text
        if (emailAddress.isEmpty()) {
            Toast.makeText(this, "Email address is required", Toast.LENGTH_SHORT).show()
            return
        }

        val password = viewBinding.registerPassword.text
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
            return
        }

        val newUser = User(firstName.toString(), lastName.toString(), emailAddress.toString(), password.toString())
        val callerIntent = intent
        callerIntent.putExtra("new-user", newUser)
        setResult(Activity.RESULT_OK, callerIntent)
        println("Hello I am here")
        finish()
    }
}