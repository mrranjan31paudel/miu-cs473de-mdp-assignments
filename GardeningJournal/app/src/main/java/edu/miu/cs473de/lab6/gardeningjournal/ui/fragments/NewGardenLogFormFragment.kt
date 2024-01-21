package edu.miu.cs473de.lab6.gardeningjournal.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import edu.miu.cs473de.lab6.gardeningjournal.R
import edu.miu.cs473de.lab6.gardeningjournal.databinding.FragmentNewGardenLogFormBinding
import edu.miu.cs473de.lab6.gardeningjournal.db.GardeningDatabase
import edu.miu.cs473de.lab6.gardeningjournal.db.entities.Plant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class NewGardenLogFormFragment() : BaseFragment(), CoroutineScope {

    private lateinit var viewBinding: FragmentNewGardenLogFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_new_garden_log_form, container, false)

        viewBinding = FragmentNewGardenLogFormBinding.bind(view)

        viewBinding.plantingDateEditText.setOnClickListener { v ->
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select planting date")
                .build()
            datePicker.addOnPositiveButtonClickListener {
                val editText = v as TextInputEditText
                val timeZone = TimeZone.getTimeZone("CST")
                val calendar = Calendar.getInstance(timeZone)
                val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
                dateFormat.timeZone = timeZone
                calendar.timeInMillis = datePicker.selection ?: 0L
                editText.setText(dateFormat.format(calendar.time))
            }
            datePicker.show(parentFragmentManager, "Planting_date_picker")
        }

        viewBinding.cancelButton.setOnClickListener{v ->
            navigateToGardenLogFragment(v)
        }

        viewBinding.saveButton.setOnClickListener { v ->
            savePlantLog(v)
        }

        return view
    }

    private fun navigateToGardenLogFragment(view: View) {
        val action = NewGardenLogFormFragmentDirections.actionNewGardenLogFormFragmentToGardenLogFragment()
        Navigation.findNavController(view).navigate(action)
    }

    private fun savePlantLog(view: View): Boolean {
        val plant = getPlantDataFromForm() ?: return false

        var jobSucceeded = true
        launch {
            context?.let {
                try {
                    GardeningDatabase(it).plantDao().createPlant(plant)
                    Snackbar.make(viewBinding.root, R.string.plant_log_save_success, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(resources.getColor(R.color.green1))
                        .setTextColor(resources.getColor(R.color.white))
                        .show()
                    navigateToGardenLogFragment(view)
                }
                catch (e: Exception) {
                    jobSucceeded = false
                    Snackbar.make(viewBinding.root, R.string.plant_log_save_failed, Snackbar.LENGTH_SHORT)
                        .setBackgroundTint(resources.getColor(R.color.red))
                        .setTextColor(resources.getColor(R.color.white))
                        .show()
                }
            }
        }

        return jobSucceeded
    }

    private fun setInputLayoutError(inputLayout: TextInputLayout, errorMessage: String?) {
        if (errorMessage == null) {
            inputLayout.error = null
            inputLayout.isErrorEnabled = false
        }
        else {
            inputLayout.isErrorEnabled = true
            inputLayout.error = errorMessage
        }
    }

    private fun getPlantDataFromForm(): Plant? {
        val plantName = viewBinding.plantName.editText?.text.toString()
        if (plantName.isEmpty() || plantName.length < 2) {
            setInputLayoutError(viewBinding.plantName, getString(R.string.plant_name_error_msg))
            return null
        }
        else {
            setInputLayoutError(viewBinding.plantName, null)
        }

        val plantType = viewBinding.plantType.editText?.text.toString()
        if (plantType.isEmpty() || plantType.length < 2) {
            setInputLayoutError(viewBinding.plantType, getString(R.string.plant_type_error_msg))
            return null
        }
        else {
            setInputLayoutError(viewBinding.plantType, null)
        }

        val wateringFrequency = viewBinding.wateringFrequency.editText?.text.toString()
        if (wateringFrequency.isEmpty() || !wateringFrequency.matches(Regex("^([1-9])|(10)$"))) {
            setInputLayoutError(viewBinding.wateringFrequency, getString(R.string.watering_frequency_error_msg))
            return null
        }
        else {
            setInputLayoutError(viewBinding.wateringFrequency, null)
        }

        val plantingDate = viewBinding.plantingDate.editText?.text.toString()
        if (plantingDate.isEmpty()) {
            setInputLayoutError(viewBinding.plantingDate, getString(R.string.planting_date_error_msg))
            return null
        }
        else {
            setInputLayoutError(viewBinding.plantingDate, null)
        }

        return Plant(0L, plantName, plantType, wateringFrequency.toInt(), plantingDate)
    }
}