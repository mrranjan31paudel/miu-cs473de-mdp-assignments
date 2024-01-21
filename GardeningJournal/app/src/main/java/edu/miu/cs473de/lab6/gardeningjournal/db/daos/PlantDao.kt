package edu.miu.cs473de.lab6.gardeningjournal.db.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import edu.miu.cs473de.lab6.gardeningjournal.data.PlantShortInfo
import edu.miu.cs473de.lab6.gardeningjournal.db.entities.Plant

@Dao
interface PlantDao {
    @Insert
    suspend fun createPlant(plant: Plant)

    @Query("SELECT id, name, type FROM Plant")
    fun getPlants(): LiveData<List<PlantShortInfo>>

    @Query("SELECT * FROM Plant WHERE id=:id")
    suspend fun getPlantDetailsById(id: Long): Plant?

    @Update
    suspend fun updatePlant(plant: Plant)

    @Delete
    suspend fun deletePlant(plant: Plant)
}
