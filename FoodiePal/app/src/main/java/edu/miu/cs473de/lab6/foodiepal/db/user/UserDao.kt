package edu.miu.cs473de.lab6.foodiepal.db.user

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.miu.cs473de.lab6.foodiepal.data.user.User

@Dao
interface UserDao {
    @Query("SELECT * FROM User WHERE email=:email")
    suspend fun getUserByEmail(email: String): User?

    @Insert
    suspend fun createUser(user: User)
}