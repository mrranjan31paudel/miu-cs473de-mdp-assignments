package edu.miu.cs473de.lab6.foodiepal.db.aboutme

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import edu.miu.cs473de.lab6.foodiepal.data.aboutme.AboutMeItem

@Dao
interface AboutMeDao {

    @Query("SELECT * FROM AboutMeItem")
    suspend fun getAll(): List<AboutMeItem>?

    @Query("SELECT * FROM AboutMeItem WHERE id=:id")
    suspend fun getById(id: Int): AboutMeItem?

    @Insert
    suspend fun addItems(vararg item: AboutMeItem): List<Long>?
}