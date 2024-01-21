package edu.miu.cs473de.lab6.gardeningjournal.ui.listeners

import android.view.View

interface OnGardenLogRecyclerViewItemClickListener {
    fun onClick(plantId: Long, view: View)
}