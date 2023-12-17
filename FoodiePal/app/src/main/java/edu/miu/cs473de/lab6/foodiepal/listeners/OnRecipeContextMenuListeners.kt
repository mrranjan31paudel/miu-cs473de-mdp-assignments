package edu.miu.cs473de.lab6.foodiepal.listeners

import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.View
import edu.miu.cs473de.lab6.foodiepal.data.recipe.Recipe
import java.text.FieldPosition

interface OnRecipeContextMenuListeners {
    fun onRecipeContextMenuCreate(menu: ContextMenu, view: View, menuInfo: ContextMenuInfo?, recipe: Recipe, position: Int)
}