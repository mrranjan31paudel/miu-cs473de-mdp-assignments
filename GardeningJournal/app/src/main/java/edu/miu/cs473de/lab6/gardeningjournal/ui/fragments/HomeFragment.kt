package edu.miu.cs473de.lab6.gardeningjournal.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import edu.miu.cs473de.lab6.gardeningjournal.R
import edu.miu.cs473de.lab6.gardeningjournal.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var viewBinding: FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        viewBinding = FragmentHomeBinding.bind(view)

        viewBinding.goToGardeningLogButton.setOnClickListener{ v ->
            val action = HomeFragmentDirections.actionHomeFragmentToGardenLogFragment()
            Navigation.findNavController(v).navigate(action)
        }

        return view
    }
}