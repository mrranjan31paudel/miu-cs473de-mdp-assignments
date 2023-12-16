package edu.miu.cs473de.lab6.foodiepal.ui.core

import android.content.Context
import android.os.Bundle
import android.view.ContextMenu
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import edu.miu.cs473de.lab6.foodiepal.OnRecipeContextMenuListeners
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.RecipeRecyclerViewAdapter
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentRecipeBinding
import edu.miu.cs473de.lab6.foodiepal.service.RecipeService

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeFragment : Fragment() {

    private lateinit var viewBinding: FragmentRecipeBinding
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var recipes: ArrayList<Recipe>
    private var loggedInUserId: Int = 0
    private var selectedRecipe: Recipe? = null
    private var selectedRecipePosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe, container, false)

        recipesRecyclerView = view.findViewById<RecyclerView>(R.id.recipes_recycler_view)
        recipesRecyclerView.layoutManager = LinearLayoutManager(activity)
        recipes = initializeRecipes()
        loggedInUserId = getLoggedInUserId()
        val activity = this.activity
        recipesRecyclerView.adapter = RecipeRecyclerViewAdapter(this, recipes, object: OnRecipeContextMenuListeners {
            override fun onRecipeContextMenuCreate(
                menu: ContextMenu,
                view: View,
                menuInfo: ContextMenu.ContextMenuInfo?,
                recipe: Recipe,
                position: Int
            ) {
                println("Hello: 1")
                val menuInflater = activity?.menuInflater ?: return
                println("Hello: 2")
                menuInflater.inflate(R.menu.context_options, menu)
                println("Hello: 3")
                val deleteOption = menu.findItem(R.id.deleteRecipeOption)
                println("Hello: 4")
                selectedRecipe = recipe
                selectedRecipePosition = position
                deleteOption.isVisible = recipe.authorId == loggedInUserId
                println("Hello: 5")
            }
        })

        return view
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val flag = when(item.itemId) {
            R.id.shareRecipeOption -> {
                onRecipeShare()
                true
            }
            R.id.deleteRecipeOption -> {
                onRecipeDelete()
                true
            }
            else -> super.onContextItemSelected(item)
        }
        return flag
    }

    private fun onRecipeShare() {
        // do social sharing
        selectedRecipe = null
        selectedRecipePosition = -1
    }

    private fun onRecipeDelete() {
        if (selectedRecipe != null && selectedRecipePosition > -1) {

            if (selectedRecipe?.authorId != loggedInUserId) {
                Snackbar.make(viewBinding.root, "Action not allowed!", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.red_1))
                    .setActionTextColor(resources.getColor(R.color.white))
                    .show()
                return
            }

            try {
                RecipeService.deleteRecipe(selectedRecipe!!)
                Snackbar.make(viewBinding.root, "Recipe deleted successfully!", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.green_1))
                    .setActionTextColor(resources.getColor(R.color.white))
                    .show()
                recipes.removeAt(selectedRecipePosition)
                recipesRecyclerView.adapter?.notifyItemRemoved(selectedRecipePosition)
            }
            catch (e: Exception) {
                Snackbar.make(viewBinding.root, "Action not allowed!", Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(resources.getColor(R.color.red_1))
                    .setActionTextColor(resources.getColor(R.color.white))
                    .show()
            }
            selectedRecipe = null
            selectedRecipePosition = -1
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentRecipeBinding.bind(view)

        viewBinding.addNewRecipeButton.setOnClickListener { v ->
            openNewRecipeFormDialog()
        }
    }

    private fun updateListWithNewRecipe(recipeId: Int) {
        val recipe = RecipeService.getRecipeById(recipeId) ?: return
        recipes.add(recipe)
        recipesRecyclerView.adapter?.notifyItemInserted((recipes.size - 1).coerceAtLeast(0))
    }

    private fun getLoggedInUserId(): Int {
        val sharedPreferences = activity?.getSharedPreferences("app_pref", Context.MODE_PRIVATE) ?: return 0

        return sharedPreferences.getInt(getString(R.string.logged_in_user_id), 0)
    }

    private fun initializeRecipes(): ArrayList<Recipe> {
        try {
            return RecipeService.getAllRecipes() as ArrayList<Recipe>
        }
        catch (e: Exception) {
            Snackbar.make(viewBinding.root, "Failed to fetch recipes!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.red_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()
            return ArrayList()
        }
    }

    private fun openNewRecipeFormDialog() {
        val fragmentManager = activity?.supportFragmentManager ?: return

        val preFragment = fragmentManager.findFragmentByTag(NewRecipeDialogFragment.TAG)
        if (preFragment != null) return

        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        val newRecipeDialogFragment = NewRecipeDialogFragment.newInstance()
        newRecipeDialogFragment.show(transaction, NewRecipeDialogFragment.TAG)

        fragmentManager.setFragmentResultListener("on_creation_success", viewLifecycleOwner) {requestKey, bundle ->
            val newRecipeId = bundle.getInt("newRecipeId")

            Snackbar.make(viewBinding.root, "Recipe created successfully!", Snackbar.LENGTH_SHORT)
                .setBackgroundTint(resources.getColor(R.color.green_1))
                .setActionTextColor(resources.getColor(R.color.white))
                .show()

            if (newRecipeId > 0) {
                updateListWithNewRecipe(newRecipeId)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment RecipeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            RecipeFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}