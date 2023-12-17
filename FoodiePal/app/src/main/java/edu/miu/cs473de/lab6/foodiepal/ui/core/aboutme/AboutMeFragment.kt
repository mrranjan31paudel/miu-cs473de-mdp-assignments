package edu.miu.cs473de.lab6.foodiepal.ui.core.aboutme

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.miu.cs473de.lab6.foodiepal.recyclerviewadapters.AboutMeRecyclerViewAdapter
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.data.aboutme.AboutMeItem
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentAboutMeBinding
import edu.miu.cs473de.lab6.foodiepal.service.abouteme.AboutMeService

/**
 * A simple [Fragment] subclass.
 * Use the [AboutMeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AboutMeFragment : Fragment() {

    private lateinit var viewBinding: FragmentAboutMeBinding
    private lateinit var aboutMeRecyclerView: RecyclerView
    private lateinit var aboutMeItems: ArrayList<AboutMeItem>
    private lateinit var loggedInUserEmail: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about_me, container, false)

        val ownerEmail = getString(R.string.owner_email)
        loggedInUserEmail = getLoggedInUserEmail()
        viewBinding = FragmentAboutMeBinding.bind(view)

        viewBinding.addNewAboutMeItemButton.isVisible = ownerEmail == loggedInUserEmail
        if (ownerEmail == loggedInUserEmail) {
            viewBinding.addNewAboutMeItemButton.setOnClickListener{ v->
                openNewAboutMeItemDialog()
            }
        }

        aboutMeRecyclerView = viewBinding.aboutMeRecyclerView
        aboutMeRecyclerView.layoutManager = LinearLayoutManager(activity)
        aboutMeItems = initializeItems()
        aboutMeRecyclerView.adapter = AboutMeRecyclerViewAdapter(aboutMeItems)

        return view
    }

    private fun getLoggedInUserEmail(): String {
        val sharedPreferences = activity?.getSharedPreferences("app_pref", Context.MODE_PRIVATE) ?: return ""

        return sharedPreferences.getString(getString(R.string.logged_in_user_email), "") ?: ""
    }

    private fun initializeItems(): ArrayList<AboutMeItem> {
        return try {
            AboutMeService.getAllItems()
        }
        catch (e: Exception) {
            Snackbar.make(viewBinding.root, "Failed to load Aboute Me!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
            ArrayList()
        }
    }

    private fun openNewAboutMeItemDialog() {
        val fragmentManager = activity?.supportFragmentManager ?: return

        val preFragment = fragmentManager.findFragmentByTag(NewAboutMeSectionFragment.TAG)
        if (preFragment != null) return

        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        val newRecipeDialogFragment = NewAboutMeSectionFragment.newInstance()
        newRecipeDialogFragment.show(transaction, NewAboutMeSectionFragment.TAG)

        fragmentManager.setFragmentResultListener("on_creation_success", viewLifecycleOwner) {requestKey, bundle ->
            val newItemId = bundle.getInt("newAboutMeItemId")
            Snackbar.make(viewBinding.root, "About Me section added successfully!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.green_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
            if (newItemId > 0) {
                updateListWithNewAboutMeSection(newItemId)
            }
        }
    }

    private fun updateListWithNewAboutMeSection(newAboutMeItemId: Int) {
        try {
            val item = AboutMeService.getItemById(newAboutMeItemId) ?: return
            aboutMeItems.add(item)
            aboutMeRecyclerView.adapter?.notifyDataSetChanged()
        }
        catch (e: Exception) {
            println("ERRORs: $e")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment AboutMeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            AboutMeFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}