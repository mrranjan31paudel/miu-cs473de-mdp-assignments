package edu.miu.cs473de.lab6.gardeningjournal.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.miu.cs473de.lab6.gardeningjournal.R
import edu.miu.cs473de.lab6.gardeningjournal.databinding.FragmentGardenLogBinding
import edu.miu.cs473de.lab6.gardeningjournal.ui.listeners.OnGardenLogRecyclerViewItemClickListener
import edu.miu.cs473de.lab6.gardeningjournal.ui.recyclerviewadapters.GardenLogRecyclerViewAdapter
import edu.miu.cs473de.lab6.gardeningjournal.ui.viewmodels.GardenLogViewModel

class GardenLogFragment : BaseFragment() {

    private lateinit var viewBinding: FragmentGardenLogBinding
    private lateinit var model: GardenLogViewModel
    private lateinit var gardenLogRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_garden_log, container, false)

        viewBinding = FragmentGardenLogBinding.bind(view)
        viewBinding.addNewLogButton.setOnClickListener{ v ->
            val action = GardenLogFragmentDirections.actionGardenLogFragmentToNewGardenLogFormFragment()
            Navigation.findNavController(v).navigate(action)
        }

        gardenLogRecyclerView = viewBinding.gardenLogRecyclerView
        gardenLogRecyclerView.layoutManager = LinearLayoutManager(activity)
        model = ViewModelProvider(requireActivity())[GardenLogViewModel::class.java]
        gardenLogRecyclerView.adapter = GardenLogRecyclerViewAdapter(model, object: OnGardenLogRecyclerViewItemClickListener {
            override fun onClick(plantId: Long, view: View) {
                navigateToPlantDetails(plantId, view)
            }
        })
        model.plants.observe(requireActivity()) {
            gardenLogRecyclerView.adapter?.notifyDataSetChanged()
        }

        return view
    }

    private fun navigateToPlantDetails(plantId: Long, view: View) {
        val action = GardenLogFragmentDirections.actionGardenLogFragmentToPlantDetailsFragment(plantId)
        Navigation.findNavController(view).navigate(action)
    }
}