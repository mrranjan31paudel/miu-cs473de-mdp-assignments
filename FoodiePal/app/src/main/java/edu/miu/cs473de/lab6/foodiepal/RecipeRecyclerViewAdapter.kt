package edu.miu.cs473de.lab6.foodiepal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe

class RecipeRecyclerViewAdapter(var context: Fragment, var recipes: ArrayList<Recipe>, var onRecipeContextMenuListeners: OnRecipeContextMenuListeners): RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipeRecyclerViewAdapter.RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipe_item_layout, parent, false)
        return RecipeViewHolder(view)
    }

    private fun getHHMMStringFromMinute(minute: Int): String {
        val hr: Int = minute / 60;
        val min: Int = minute % 60;

        return "$hr".padStart(2, '0') + ":" + "$min".padStart(2, '0')
    }

    override fun onBindViewHolder(holder: RecipeRecyclerViewAdapter.RecipeViewHolder, position: Int) {
        var imgSrc = recipes[position].imgSrc

        if (imgSrc == 0) {
            imgSrc = R.drawable.fallback
        }
        holder.recipeImage.setImageResource(imgSrc)
        holder.recipeName.text = recipes[position].name
        holder.recipeCookingTime.text = getHHMMStringFromMinute(recipes[position].cookingTimeInMin)
        holder.recipeRating.text = context.getString(R.string.rating_total_suffix, recipes[position].rating.toString()) ?: "N/A"
        holder.recipeDesc.text = recipes[position].description
        holder.recipeImage.contentDescription = "${recipes[position].name} image"
        holder.authorName.text = "by ${recipes[position].authorName ?: "Unknown"}"
        context.registerForContextMenu(holder.recipeContainer)
        holder.recipeContainer.setOnCreateContextMenuListener { menu, v, menuInfo ->
            println("Hello: " + menu)
            println("Hello: " + v)
            println("Hello: " + menuInfo)
            println("Hello: " + recipes[position])
            println("Hello: " + position)

            onRecipeContextMenuListeners.onRecipeContextMenuCreate(menu, v, menuInfo, recipes[position], position)
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

    inner class RecipeViewHolder(recipeView: View): RecyclerView.ViewHolder(recipeView) {
        var recipeImage: ImageView = recipeView.findViewById(R.id.item_recipe_image)
        var recipeName: TextView = recipeView.findViewById(R.id.item_recipe_name)
        var recipeCookingTime: TextView = recipeView.findViewById(R.id.item_recipe_cooking_time)
        var recipeRating: TextView = recipeView.findViewById(R.id.item_recipe_rating)
        var recipeDesc: TextView = recipeView.findViewById(R.id.item_recipe_description)
        var authorName: TextView = recipeView.findViewById(R.id.itemRecipeAuthorName)
        var recipeContainer: ConstraintLayout = recipeView.findViewById(R.id.recipeItemContainer)
    }
}
