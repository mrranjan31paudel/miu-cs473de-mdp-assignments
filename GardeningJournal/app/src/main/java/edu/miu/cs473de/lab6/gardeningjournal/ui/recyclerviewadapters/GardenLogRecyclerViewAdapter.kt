package edu.miu.cs473de.lab6.gardeningjournal.ui.recyclerviewadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import edu.miu.cs473de.lab6.gardeningjournal.R
import edu.miu.cs473de.lab6.gardeningjournal.ui.listeners.OnGardenLogRecyclerViewItemClickListener
import edu.miu.cs473de.lab6.gardeningjournal.ui.viewmodels.GardenLogViewModel

class GardenLogRecyclerViewAdapter(var model: GardenLogViewModel, var itemClickListener: OnGardenLogRecyclerViewItemClickListener): RecyclerView.Adapter<GardenLogRecyclerViewAdapter.GardenLogViewHolder>() {

    class GardenLogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var plantName: TextView = itemView.findViewById(R.id.plantName)
        var plantType: TextView = itemView.findViewById(R.id.plantType)
        var container: MaterialCardView = itemView.findViewById(R.id.plantItemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenLogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.garden_log_plant_item, parent, false)
        return GardenLogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return model.plants.value?.size ?: 0
    }

    override fun onBindViewHolder(holder: GardenLogViewHolder, position: Int) {
        val plantShortInfo = model.plants.value?.get(position)
        holder.plantName.text = plantShortInfo?.name ?: ""
        holder.plantType.text = plantShortInfo?.type ?: ""
        holder.container.setOnClickListener{ v -> itemClickListener.onClick(plantShortInfo?.id ?: 0L, v) }
    }
}