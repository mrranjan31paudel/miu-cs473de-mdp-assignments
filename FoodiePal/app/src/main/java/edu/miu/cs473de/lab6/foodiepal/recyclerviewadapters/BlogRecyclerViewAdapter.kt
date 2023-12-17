package edu.miu.cs473de.lab6.foodiepal.recyclerviewadapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import edu.miu.cs473de.lab6.foodiepal.listeners.OnBlogItemViewClickListener
import edu.miu.cs473de.lab6.foodiepal.R
import edu.miu.cs473de.lab6.foodiepal.data.blog.Blog

class BlogRecyclerViewAdapter(var blogs: ArrayList<Blog>, var onBlogItemViewClickListener: OnBlogItemViewClickListener): RecyclerView.Adapter<BlogRecyclerViewAdapter.BlogViewHolder>() {

    inner class BlogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var titleView: TextView = itemView.findViewById(R.id.blogItemTitle)
        var authorAndTimeView: TextView = itemView.findViewById(R.id.blogItemAuthorAndTime)
        var container: MaterialCardView = itemView.findViewById(R.id.blogItemContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.blog_item_layout, parent, false)
        return BlogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return blogs.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        holder.titleView.text = blogs[position].title
        holder.authorAndTimeView.text = "By: ${blogs[position].authorName}, At: ${blogs[position].postedAtString ?: ""}"
        holder.container.setOnClickListener { v -> onBlogItemViewClickListener.onBlogViewClick(blogs[position], position) }
    }
}