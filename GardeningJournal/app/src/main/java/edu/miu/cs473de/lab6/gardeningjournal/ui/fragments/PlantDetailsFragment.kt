package edu.miu.cs473de.lab6.gardeningjournal.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import edu.miu.cs473de.lab6.gardeningjournal.R
import edu.miu.cs473de.lab6.gardeningjournal.databinding.FragmentPlantDetailsBinding
import edu.miu.cs473de.lab6.gardeningjournal.db.GardeningDatabase
import edu.miu.cs473de.lab6.gardeningjournal.db.entities.Plant
import edu.miu.cs473de.lab6.gardeningjournal.ui.viewmodels.PlantDetailsViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlantDetailsFragment : BaseFragment() {

    val args: PlantDetailsFragmentArgs by navArgs()
    private lateinit var model: PlantDetailsViewModel
    private lateinit var viewBinding: FragmentPlantDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_plant_details, container, false)

        viewBinding = FragmentPlantDetailsBinding.bind(view)
        model = ViewModelProvider(requireActivity())[PlantDetailsViewModel::class.java]
        model.plant.observe(requireActivity()) {
            updateView(it)
        }

        if ((model.plant.value?.id ?: 0L) != args.plantId) {
            model.reset()
            setViewsVisibility(false)
            loadPlantDetails(args.plantId)
        }

        viewBinding.deletePlantButton.setOnClickListener { v ->
            val plant = model.plant.value
            if (plant != null && plant.id != 0L) {
                deletePlant(plant)
            }
        }

        return view
    }

    private fun setViewsVisibility(isVisible: Boolean) {
        viewBinding.plantType.isVisible = isVisible
        viewBinding.wateringFrequency.isVisible = isVisible
        viewBinding.plantingDate.isVisible = isVisible
        viewBinding.deletePlantButton.isVisible = isVisible
    }

    private fun deletePlant(plant: Plant) {
        launch {
            context?.let {
                try {
                    GardeningDatabase(it).plantDao().deletePlant(plant)
                    Snackbar.make(viewBinding.root, R.string.plant_log_delete_success, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(resources.getColor(R.color.green1))
                        .setTextColor(resources.getColor(R.color.white))
                        .show()
                    navigateToGardenLog()
                }
                catch (e: Exception) {
                    Snackbar.make(viewBinding.root, R.string.plant_log_delete_failed, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(resources.getColor(R.color.red))
                        .setTextColor(resources.getColor(R.color.white))
                        .show()
                }
            }
        }
    }

    private fun updateView(plant: Plant?) {
        if (plant == null) return

        viewBinding.plantName.text = plant.name

        if (plant.id == 0L) return

        viewBinding.plantType.text = plant.type
        viewBinding.wateringFrequency.text = "Watering Frequency: ${plant.wateringFrequency} per day"
        viewBinding.plantingDate.text = "Planting Date: ${plant.plantingDate}"
    }

    private fun populateLiveDataModel(plant: Plant?) {
        if (plant == null) {
            model.updatePlant(Plant(0L, "Not Found", "", 0, ""))
        }
        else {
            model.updatePlant(plant)
            setViewsVisibility(true)
        }
    }

    private fun loadPlantDetails(plantId: Long) {
        launch {
            delay(2000L)
            context?.let {
                try {
                    val plant = GardeningDatabase(it).plantDao().getPlantDetailsById(plantId)
                    populateLiveDataModel(plant)
                }
                catch (e: Exception) {
                    populateLiveDataModel(null)
                    Snackbar.make(viewBinding.root, R.string.plant_detail_load_failed, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(resources.getColor(R.color.red))
                        .setTextColor(resources.getColor(R.color.white))
                        .show()
                }
            }
        }
    }

    private fun navigateToGardenLog() {
        val action = PlantDetailsFragmentDirections.actionPlantDetailsFragmentToGardenLogFragment()
        Navigation.findNavController(viewBinding.root).navigate(action)
    }
}