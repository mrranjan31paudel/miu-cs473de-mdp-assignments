package edu.miu.cs473de.lab6.gardeningjournal.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import edu.miu.cs473de.lab6.gardeningjournal.data.PlantShortInfo
import edu.miu.cs473de.lab6.gardeningjournal.db.GardeningDatabase

class GardenLogViewModel(application: Application): AndroidViewModel(application) {
    val plants: LiveData<List<PlantShortInfo>>

    init {
        val plantDao = GardeningDatabase(application).plantDao()
        plants = plantDao.getPlants()
    }
}