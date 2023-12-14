package edu.miu.cs473de.lab6.foodiepal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe

class RecipeRecyclerViewAdapter(var context: Context?, var recipes: ArrayList<Recipe>): RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder>() {
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
        holder.recipeRating.text = context?.getString(R.string.rating_total_suffix, recipes[position].rating.toString()) ?: "N/A"
        holder.recipeDesc.text = recipes[position].description
        holder.recipeImage.contentDescription = "${recipes[position].name} image"
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
    }
}
