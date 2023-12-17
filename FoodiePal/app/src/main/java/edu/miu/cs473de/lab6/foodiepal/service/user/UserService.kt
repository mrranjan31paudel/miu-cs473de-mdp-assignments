package edu.miu.cs473de.lab6.foodiepal.service.user

import android.util.Patterns
import edu.miu.cs473de.lab6.foodiepal.data.user.User
import edu.miu.cs473de.lab6.foodiepal.errors.ValidationException
import edu.miu.cs473de.lab6.foodiepal.service.DatabaseService
import kotlinx.coroutines.runBlocking

class UserService {
    companion object {
        private val nameRegex = Regex("^[a-zA-Z]{2,}$")
        private fun validateNewUserData(firstName: String, lastName: String, email: String, password: String, confirmPassword: String) {
            if (firstName.isEmpty()) {
                throw ValidationException("firstName", "Required with at least 2 characters!")
            }
            if (!firstName.matches(nameRegex)) {
                throw ValidationException("firstName", "Only alphabets allowed!")
            }
            if (lastName.isEmpty()) {
                throw ValidationException("lastName", "Required with at least 2 characters!")
            }
            if (!lastName.matches(nameRegex)) {
                throw ValidationException("lastName", "Only alphabets allowed!")
            }
            if (email.isEmpty()) {
                throw ValidationException("email", "Required!")
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                throw ValidationException("email", "Invalid format!")
            }
            if (password.isEmpty() || password.length < 8) {
                throw ValidationException("password", "Required with at least 8 characters!")
            }
            if (password != confirmPassword) {
                throw ValidationException("confirmPassword", "Does not match with 'Password'!")
            }
        }

        private fun validateUserLoginData(email: String, password: String) {
            if (email.isEmpty()) {
                throw ValidationException("email", "Required!")
            }
            if (password.isEmpty()) {
                throw ValidationException("password", "Required!")
            }
        }

        fun createUser(firstName: String, lastName: String, email: String, password: String, confirmPassword: String) {
            validateNewUserData(firstName, lastName, email, password, confirmPassword)

            val userDao = DatabaseService.db?.userDao()
            val user = User(0, firstName, lastName, email, password)
            runBlocking {
                userDao?.createUser(user)
            }
        }

        fun loginUser(email: String, password: String): User {
            validateUserLoginData(email, password)

            val userDao = DatabaseService.db?.userDao()
            val existingUser = runBlocking {
                val user = userDao?.getUserByEmail(email) ?: throw Exception("Account does not exist!")

                if (user.password != password) {
                    throw ValidationException("password", "Email and password do not match!")
                }

                return@runBlocking user
            }

            return existingUser
        }

        fun getUserByEmail(email: String): User? {
            val userDao = DatabaseService.db?.userDao()
            val user = runBlocking {
                return@runBlocking userDao?.getUserByEmail(email)
            }

            return user
        }
    }
}