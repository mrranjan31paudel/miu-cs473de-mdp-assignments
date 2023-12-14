package edu.miu.cs473de.lab6.foodiepal.ui.core

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.appbar.MaterialToolbar
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.RecipeRecyclerViewAdapter
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe
import edu.miu.cs473de.lab6.foodiepal.databinding.FragmentRecipeBinding
import edu.miu.cs473de.lab6.foodiepal.service.RecipeService
import edu.miu.cs473de.lab6.foodiepal.ui.auth.RegisterFragment

/**
 * A simple [Fragment] subclass.
 * Use the [RecipeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipeFragment : Fragment() {

    private lateinit var viewBinding: FragmentRecipeBinding
    private lateinit var recipesRecyclerView: RecyclerView
    private lateinit var recipes: ArrayList<Recipe>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe, container, false)

        recipesRecyclerView = view.findViewById<RecyclerView>(R.id.recipes_recycler_view)
        recipesRecyclerView.layoutManager = LinearLayoutManager(activity)
        recipes = initializeRecipes()
        recipesRecyclerView.adapter = RecipeRecyclerViewAdapter(context, recipes)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding = FragmentRecipeBinding.bind(view)

        viewBinding.addNewRecipeButton.setOnClickListener{v ->
            openNewRecipeFormDialog()
        }
    }

    private fun updateListWithNewRecipe(recipeId: Int) {
        val recipe = RecipeService.getRecipeById(recipeId) ?: return
        recipes.add(recipe)
        recipesRecyclerView.adapter?.notifyItemInserted(recipes.size - 1)
    }

    private fun initializeRecipes(): ArrayList<Recipe> {
        val recipesInDb = RecipeService.getCount()

        if (recipesInDb > 0) {
            return RecipeService.getAllRecipes() as ArrayList<Recipe>
        }

        val recipes = ArrayList<Recipe>()

        recipes.add(Recipe(0, R.drawable.pizza, "Pizza", 60, 4.5f, "Ingredients: flour, cheese, marinara", 1))
        recipes.add(Recipe(0, R.drawable.pizza, "Pizza", 60, 4.5f, "Ingredients: flour, cheese, marinara sauce, chicken, olives", 1))
        recipes.add(Recipe(0, R.drawable.pizza, "Pizza", 60, 4.5f, "Ingredients: flour, cheese, marinara sauce, chicken, olives", 1))
        recipes.add(Recipe(0, R.drawable.pizza, "Pizza", 60, 4.5f, "Ingredients: flour, cheese, marinara sauce, chicken, olives", 1))
        recipes.add(Recipe(0, R.drawable.pizza, "Pizza", 60, 4.5f, "Ingredients: flour, cheese, marinara sauce, chicken, olives", 1))
        recipes.add(Recipe(0, R.drawable.pizza, "Pizza", 60, 4.5f, "Ingredients: flour, cheese, marinara sauce, chicken, olives", 1))
        recipes.add(Recipe(0, R.drawable.pizza, "Pizza", 60, 4.5f, "Ingredients: flour, cheese, marinara sauce, chicken, olives", 1))

        return recipes
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