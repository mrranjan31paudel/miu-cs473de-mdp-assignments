package edu.miu.cs473de.lab6.foodiepal.ui.core

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.appbar.MaterialToolbar
import edu.miu.cs473de.lab6.foodiepal.R

/**
 * A simple [Fragment] subclass.
 * Use the [BlogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blog, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment BlogFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            BlogFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}