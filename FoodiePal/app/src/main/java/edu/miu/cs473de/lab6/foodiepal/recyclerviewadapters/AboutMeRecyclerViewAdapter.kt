package edu.miu.cs473de.lab6.foodiepal.recyclerviewadapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.data.aboutme.AboutMeItem

class AboutMeRecyclerViewAdapter(var items: ArrayList<AboutMeItem>): RecyclerView.Adapter<AboutMeRecyclerViewAdapter.AboutMeItemViewHolder>() {

    inner class AboutMeItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var titleView: TextView = itemView.findViewById(R.id.aboutMeItemTitle)
        var descriptionView: TextView = itemView.findViewById(R.id.aboutMeItemDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutMeItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.abouteme_item_layout, parent, false)
        return AboutMeItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AboutMeItemViewHolder, position: Int) {
        holder.titleView.text = items[position].title
        holder.descriptionView.text = items[position].description
    }
}