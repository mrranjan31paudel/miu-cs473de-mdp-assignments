package edu.miu.cs473de.lab6.gardeningjournal.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.miu.cs473de.lab6.gardeningjournal.db.entities.Plant

class PlantDetailsViewModel: ViewModel() {
    var plant = MutableLiveData<Plant>()
    init {
        plant.value = Plant(0L, "Loading...", "", 0, "")
    }

    fun updatePlant(p: Plant) {
        plant.value = p
    }

    fun reset() {
        plant.value = Plant(0L, "Loading...", "", 0, "")
    }
}